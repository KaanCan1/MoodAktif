package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    // Tercihler dosya adı ve anahtar
    private static final String PREFS_NAME = "app_prefs";
    private static final String FIRST_START_KEY = "firstStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) SharedPreferences'den "ilk açılış" bilgisini oku
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean(FIRST_START_KEY, true);

        if (!firstStart) {
            // Eğer daha önce açıldıysa direkt LoginActivity'e git
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // 2) Aksi halde Intro ekranını göster
        setContentView(R.layout.activity_intro);

        // 3) "Hadi Başlayalım" butonuna tıklama olayını dinle
        TextView startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> {
            // İlk açılış bilgisini false yap ve kaydet
            prefs.edit()
                    .putBoolean(FIRST_START_KEY, false)
                    .apply();

            // LoginActivity'i başlat
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
