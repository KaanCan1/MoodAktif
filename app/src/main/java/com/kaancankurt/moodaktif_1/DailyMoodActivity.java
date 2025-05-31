package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyMoodActivity extends AppCompatActivity {

    private RadioGroup radioGroupMood;
    private Button btnSaveMood;
    private TextView txtMoodHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_mood);

        initializeViews();
        setupToolbar();
        setupClickListeners();
        loadMoodHistory();
    }

    private void initializeViews() {
        radioGroupMood = findViewById(R.id.radioGroupMood);
        btnSaveMood = findViewById(R.id.btnSaveMood);
        txtMoodHistory = findViewById(R.id.txtMoodHistory);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Günlük Ruh Hali");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupClickListeners() {
        btnSaveMood.setOnClickListener(v -> {
            int selectedId = radioGroupMood.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedMood = findViewById(selectedId);
                String moodText = selectedMood.getText().toString();
                saveMood(moodText);
                Toast.makeText(this, "Ruh haliniz kaydedildi: " + moodText, Toast.LENGTH_SHORT).show();
                loadMoodHistory();
            } else {
                Toast.makeText(this, "Lütfen bir ruh hali seçin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMood(String mood) {
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        getSharedPreferences("mood_data", MODE_PRIVATE)
                .edit()
                .putString("mood_" + System.currentTimeMillis(), mood + " - " + today)
                .apply();
    }

    private void loadMoodHistory() {
        StringBuilder history = new StringBuilder("Ruh Hali Geçmişi:\n\n");

        // Get last 5 mood entries
        String[] moodKeys = getSharedPreferences("mood_data", MODE_PRIVATE)
                .getAll()
                .keySet()
                .toArray(new String[0]);

        if (moodKeys.length == 0) {
            txtMoodHistory.setText("Henüz ruh hali kaydı bulunmuyor.");
            return;
        }

        // Sort by timestamp (keys contain timestamp)
        java.util.Arrays.sort(moodKeys, (a, b) -> {
            long timeA = Long.parseLong(a.replace("mood_", ""));
            long timeB = Long.parseLong(b.replace("mood_", ""));
            return Long.compare(timeB, timeA); // Reverse order (newest first)
        });

        int count = Math.min(5, moodKeys.length);
        for (int i = 0; i < count; i++) {
            String moodEntry = getSharedPreferences("mood_data", MODE_PRIVATE)
                    .getString(moodKeys[i], "");
            history.append("• ").append(moodEntry).append("\n");
        }

        txtMoodHistory.setText(history.toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}