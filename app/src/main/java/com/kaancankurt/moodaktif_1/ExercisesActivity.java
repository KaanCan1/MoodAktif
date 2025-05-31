package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ExercisesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        setupToolbar();
        setupContent();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Egzersizler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        TextView content = findViewById(R.id.txtContent);
        content.setText("💪 Günlük Egzersiz Önerileri:\n\n" +
                "🏃‍♂️ Kardiyovasküler Egzersizler:\n" +
                "• 20-30 dakika hızlı yürüyüş\n" +
                "• 15 dakika koşu\n" +
                "• Merdiven çıkma\n" +
                "• Zıplama hareketi\n\n" +
                "🤸‍♀️ Esneklik Egzersizleri:\n" +
                "• Yoga pozları\n" +
                "• Gerinme hareketleri\n" +
                "• Boyun ve omuz germe\n\n" +
                "💪 Güçlendirme Egzersizleri:\n" +
                "• Şınav (10-15 tekrar)\n" +
                "• Mekik (15-20 tekrar)\n" +
                "• Squat (15-20 tekrar)\n" +
                "• Plank (30-60 saniye)\n\n" +
                "🧘‍♀️ Rahatlatıcı Egzersizler:\n" +
                "• Derin nefes egzersizleri\n" +
                "• Meditasyon\n" +
                "• Progressive kas gevşetme");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}