package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kaancankurt.moodaktif_1.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcıdan gelen bilgileri al
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                // Şifre kontrolü
                if (password.equals(confirmPassword)) {
                    // Kayıt işlemi yapılabilir
                    registerUser(name, email, password);
                } else {
                    // Şifreler uyuşmuyor
                    Toast.makeText(RegisterActivity.this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        // Kayıt işlemi burada yapılabilir
        // Örneğin, Firebase veya diğer bir veri tabanı kullanılabilir
        // Şu an basit bir Toast mesajı gösterelim
        Toast.makeText(this, "Kullanıcı başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
    }
}
