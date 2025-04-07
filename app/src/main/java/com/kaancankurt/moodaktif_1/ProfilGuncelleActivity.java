package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilGuncelleActivity extends AppCompatActivity {

    EditText editFullName, editEmail, oldPassword, editPassword;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_bilgileriguncelle); // XML dosyanın adı aynı kalabilir

        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        oldPassword = findViewById(R.id.oldPassword);
        editPassword = findViewById(R.id.editPassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(v -> {
            String name = editFullName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String oldPass = oldPassword.getText().toString().trim();
            String newPass = editPassword.getText().toString().trim();

            // Geçici bilgi mesajı
            Toast.makeText(this, "Bilgiler güncellendi (taslak)", Toast.LENGTH_SHORT).show();

            // TODO: Veritabanı işlemleri buraya gelecek
        });
    }
}
