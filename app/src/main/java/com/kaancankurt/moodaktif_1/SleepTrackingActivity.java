package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepTrackingActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSaveSleepTime;
    private TextView txtSleepHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracking);

        initializeViews();
        setupToolbar();
        setupClickListeners();
    }

    private void initializeViews() {
        timePicker = findViewById(R.id.timePicker);
        btnSaveSleepTime = findViewById(R.id.btnSaveSleepTime);
        txtSleepHistory = findViewById(R.id.txtSleepHistory);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Uyku Takibi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupClickListeners() {
        btnSaveSleepTime.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Uyku saatini kaydet
            String sleepTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            // SharedPreferences'a kaydet
            getSharedPreferences("sleep_data", MODE_PRIVATE)
                    .edit()
                    .putString("last_sleep_time", sleepTime)
                    .putLong("last_sleep_date", System.currentTimeMillis())
                    .apply();

            Toast.makeText(this, "Uyku saati kaydedildi: " + sleepTime, Toast.LENGTH_SHORT).show();
            updateSleepHistory();
        });
    }

    private void updateSleepHistory() {
        String lastSleepTime = getSharedPreferences("sleep_data", MODE_PRIVATE)
                .getString("last_sleep_time", "Kayıt yok");
        long lastSleepDate = getSharedPreferences("sleep_data", MODE_PRIVATE)
                .getLong("last_sleep_date", 0);

        if (lastSleepDate > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateStr = dateFormat.format(new Date(lastSleepDate));
            txtSleepHistory.setText("Son uyku saati: " + lastSleepTime + "\nTarih: " + dateStr);
        } else {
            txtSleepHistory.setText("Henüz uyku kaydı bulunmuyor.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSleepHistory();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}