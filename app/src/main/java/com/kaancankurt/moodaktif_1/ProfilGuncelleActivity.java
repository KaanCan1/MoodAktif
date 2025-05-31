package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilGuncelleActivity extends AppCompatActivity {

    private EditText editFullName, editEmail, oldPassword, editPassword, confirmPassword;
    private AppCompatButton btnUpdate, btnCancel;
    private Toolbar toolbar;
    private AppDatabase database;
    private ExecutorService executorService;
    private SessionManager sessionManager;
    private User currentUser;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_bilgileriguncelle);

        initializeViews();
        initializeDatabase();
        setupToolbar();
        setupBottomNavigation();
        loadCurrentUserData();
        setupClickListeners();
    }

    private void initializeViews() {
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        oldPassword = findViewById(R.id.oldPassword);
        editPassword = findViewById(R.id.editPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void initializeDatabase() {
        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    return true;
                case R.id.profile:
                    // Zaten profil sayfasındayız
                    return true;
                case R.id.settings:
                    Intent settingsIntent = new Intent(this, MainActivity.class);
                    settingsIntent.putExtra("fragment", "settings");
                    settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(settingsIntent);
                    return true;
            }
            return false;
        });
    }

    private void loadCurrentUserData() {
        if (!sessionManager.isLoggedIn()) {
            finish();
            return;
        }

        int userId = sessionManager.getUserId();
        executorService.execute(() -> {
            try {
                currentUser = database.userDao().getUserById(userId);
                runOnUiThread(() -> {
                    if (currentUser != null) {
                        editFullName.setText(currentUser.getName());
                        editEmail.setText(currentUser.getEmail());
                    } else {
                        Toast.makeText(this, "Kullanıcı bilgileri yüklenemedi", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Veritabanı hatası", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void setupClickListeners() {
        btnUpdate.setOnClickListener(v -> performUpdate());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void performUpdate() {
        String newName = editFullName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();
        String oldPass = oldPassword.getText().toString();
        String newPass = editPassword.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        // Temel validasyonlar
        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Ad soyad ve e-posta alanları boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newName.length() < 2) {
            Toast.makeText(this, "İsim en az 2 karakter olmalıdır", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(newEmail)) {
            Toast.makeText(this, "Lütfen geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Şifre değişikliği kontrolü
        boolean isPasswordChange = !oldPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty();

        if (isPasswordChange) {
            if (oldPass.isEmpty()) {
                Toast.makeText(this, "Mevcut şifrenizi girmelisiniz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPass.length() < 6) {
                Toast.makeText(this, "Yeni şifre en az 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Yeni şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        btnUpdate.setEnabled(false);
        btnUpdate.setText("Güncelleniyor...");

        executorService.execute(() -> {
            try {
                // E-posta değişikliği kontrolü
                if (!newEmail.equals(currentUser.getEmail())) {
                    int emailExists = database.userDao().isEmailExists(newEmail);
                    if (emailExists > 0) {
                        runOnUiThread(() -> {
                            btnUpdate.setEnabled(true);
                            btnUpdate.setText("Profil Güncelle");
                            Toast.makeText(this, "Bu e-posta adresi zaten kullanılıyor", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                }

                // Şifre değişikliği varsa mevcut şifreyi kontrol et
                if (isPasswordChange) {
                    if (!PasswordUtils.checkPassword(oldPass, currentUser.getPasswordHash())) {
                        runOnUiThread(() -> {
                            btnUpdate.setEnabled(true);
                            btnUpdate.setText("Profil Güncelle");
                            Toast.makeText(this, "Mevcut şifreniz yanlış", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                }

                // Kullanıcı bilgilerini güncelle
                currentUser.setName(newName);
                currentUser.setEmail(newEmail);

                if (isPasswordChange) {
                    currentUser.setPasswordHash(PasswordUtils.hashPassword(newPass));
                }

                database.userDao().updateUser(currentUser);

                // Session'ı güncelle
                sessionManager.createLoginSession(currentUser);

                runOnUiThread(() -> {
                    btnUpdate.setEnabled(true);
                    btnUpdate.setText("Profil Güncelle");
                    Toast.makeText(this, "Profil başarıyla güncellendi!", Toast.LENGTH_LONG).show();

                    // MainActivity'ye dön
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnUpdate.setEnabled(true);
                    btnUpdate.setText("Profil Güncelle");
                    Toast.makeText(this, "Güncelleme sırasında bir hata oluştu", Toast.LENGTH_SHORT).show();
                });
            }
        });
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
