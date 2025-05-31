package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendationResultActivity extends AppCompatActivity {

    private LinearLayout recommendationsContainer;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_result);

        initializeViews();
        displayRecommendations();
    }

    private void initializeViews() {
        recommendationsContainer = findViewById(R.id.recommendationsContainer);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());
    }

    private void displayRecommendations() {
        ArrayList<Recommendation> recommendations = getIntent().getParcelableArrayListExtra("recommendations");
        HashMap<String, String> surveyData = (HashMap<String, String>) getIntent().getSerializableExtra("survey_data");

        if (recommendations != null && !recommendations.isEmpty()) {
            for (int i = 0; i < recommendations.size(); i++) {
                Recommendation recommendation = recommendations.get(i);
                addRecommendationView(recommendation, i + 1);
            }
        } else {
            // Hiç öneri yoksa genel bir mesaj göster
            Recommendation defaultRecommendation = new Recommendation(
                    "Kişiselleştirilmiş öneriler için lütfen soruları eksiksiz yanıtlayın.",
                    "Genel sağlık ve mutluluk için düzenli egzersiz yapın ve sosyal bağlarınızı güçlendirin."
            );
            addRecommendationView(defaultRecommendation, 1);
        }
    }

    private void addRecommendationView(Recommendation recommendation, int index) {
        // Ana container
        LinearLayout recommendationLayout = new LinearLayout(this);
        recommendationLayout.setOrientation(LinearLayout.VERTICAL);
        recommendationLayout.setPadding(32, 24, 32, 24);
        recommendationLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 32);
        recommendationLayout.setLayoutParams(layoutParams);

        // Öneri başlığı
        TextView titleView = new TextView(this);
        titleView.setText("Öneri " + index);
        titleView.setTextSize(20);
        titleView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        titleView.setPadding(0, 0, 0, 16);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);

        // Öneri metni
        TextView textView = new TextView(this);
        textView.setText(recommendation.getText());
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setPadding(0, 0, 0, 16);

        // Açıklama metni
        TextView descriptionView = new TextView(this);
        descriptionView.setText(recommendation.getDescription());
        descriptionView.setTextSize(16);
        descriptionView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        descriptionView.setTypeface(null, android.graphics.Typeface.ITALIC);

        // View'ları container'a ekle
        recommendationLayout.addView(titleView);
        recommendationLayout.addView(textView);
        recommendationLayout.addView(descriptionView);

        // Ana container'a ekle
        recommendationsContainer.addView(recommendationLayout);
    }
}