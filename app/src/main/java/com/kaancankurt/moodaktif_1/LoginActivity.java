package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private AppCompatButton loginButton;
    private TextView registerTextView, forgotPasswordTextView;
    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeDatabase();
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
    }

    private void initializeDatabase() {
        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> performLogin());

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Toast.makeText(this, "Şifre sıfırlama özelliği yakında eklenecek", Toast.LENGTH_SHORT).show();
        });
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Lütfen geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Giriş yapılıyor...");

        executorService.execute(() -> {
            try {
                User user = database.userDao().getUserByEmail(email);

                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Giriş Yap");

                    if (user != null && PasswordUtils.checkPassword(password, user.getPasswordHash())) {
                        // Başarılı giriş
                        saveUserSession(user);
                        Toast.makeText(this, "Hoş geldiniz, " + user.getName() + "!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "E-posta veya şifre hatalı", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Giriş Yap");
                    Toast.makeText(this, "Giriş yapılırken bir hata oluştu", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void saveUserSession(User user) {
        SharedPreferences sharedPref = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("user_name", user.getName());
        editor.putString("user_email", user.getEmail());
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
