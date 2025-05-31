package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView loginTextView;
    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initializeViews();
        initializeDatabase();
        setupClickListeners();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        loginTextView = findViewById(R.id.loginTextView);
    }

    private void initializeDatabase() {
        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();
    }

    private void setupClickListeners() {
        buttonRegister.setOnClickListener(v -> performRegistration());

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegistration() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // Validasyon kontrolleri
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() < 2) {
            Toast.makeText(this, "İsim en az 2 karakter olmalıdır", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Lütfen geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Şifre en az 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonRegister.setEnabled(false);
        buttonRegister.setText("Hesap oluşturuluyor...");

        executorService.execute(() -> {
            try {
                // E-posta kontrolü
                int emailExists = database.userDao().isEmailExists(email);

                if (emailExists > 0) {
                    runOnUiThread(() -> {
                        buttonRegister.setEnabled(true);
                        buttonRegister.setText("Hesap Oluştur");
                        Toast.makeText(this, "Bu e-posta adresi zaten kullanılıyor", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Yeni kullanıcı oluştur
                User newUser = new User();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPasswordHash(PasswordUtils.hashPassword(password));

                long userId = database.userDao().insertUser(newUser);
                newUser.setId((int) userId);

                runOnUiThread(() -> {
                    buttonRegister.setEnabled(true);
                    buttonRegister.setText("Hesap Oluştur");

                    if (userId > 0) {
                        // Başarılı kayıt
                        saveUserSession(newUser);
                        Toast.makeText(this, "Hoş geldiniz, " + name + "! Hesabınız oluşturuldu.", Toast.LENGTH_LONG).show();

                        // Psikoloji anketine yönlendir
                        Intent intent = new Intent(RegisterActivity.this, PsychologySurveyActivity.class);
                        intent.putExtra("is_first_time", true);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Hesap oluşturulamadı, lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    buttonRegister.setEnabled(true);
                    buttonRegister.setText("Hesap Oluştur");
                    Toast.makeText(this, "Kayıt işlemi sırasında bir hata oluştu", Toast.LENGTH_SHORT).show();
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
