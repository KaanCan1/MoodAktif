package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ActivitySuggestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        setupToolbar();
        setupContent();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aktivite Önerileri");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        TextView content = findViewById(R.id.txtContent);
        content.setText("🎯 Günlük Aktivite Önerileri:\n\n" +
                "• 30 dakika yürüyüş yapın\n" +
                "• 10 dakika meditasyon yapın\n" +
                "• Bir bardak su için\n" +
                "• Derin nefes egzersizleri yapın\n" +
                "• Sevdiğiniz müzik dinleyin\n" +
                "• Arkadaşlarınızla sohbet edin\n" +
                "• Günlük tutun\n" +
                "• Doğada vakit geçirin\n" +
                "• Yeni bir şey öğrenin\n" +
                "• Sevdiğiniz hobinizle ilgilenin");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}