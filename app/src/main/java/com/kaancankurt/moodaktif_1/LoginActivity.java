// LoginActivity.java
package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // rename your second XML to activity_login.xml

        EditText emailEt = findViewById(R.id.editTextTextPersonName);
        EditText passEt  = findViewById(R.id.editTextTextPassword);
        AppCompatButton loginBtn = findViewById(R.id.button);

        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim();
            String pass  = passEt.getText().toString();

            // TODO: validate credentials (Firebase/Auth, etc.)
            // For now, just proceed:
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // so back button won't return to login
        });
        // Tıklanabilir TextView'nin tıklama olayını ayarlama
        TextView textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(v -> {
            // RegisterActivity'ye yönlendirme
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
