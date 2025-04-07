package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PsychologySurveyActivity extends AppCompatActivity {

    private String[] questions = {
            "Son zamanlarda kendinizi enerjik hissediyor musunuz?",
            "Sık sık endişeli veya gergin hissediyor musunuz?",
            "Uyku düzeninizde bozulma yaşadınız mı?",
            "Kendinizi değersiz hissettiğiniz zamanlar oluyor mu?",
            "Konsantrasyon sorunu yaşıyor musunuz?",
            "Günlük aktivitelerden keyif alabiliyor musunuz?",
            "Sosyal ortamlarda rahat mısınız?",
            "Kendinize karşı eleştirel misiniz?",
            "Geleceğe dair umudunuz var mı?",
            "Yalnız kalmak size iyi geliyor mu?"
    };

    private int currentQuestion = 0;
    private int[] answers = new int[questions.length];

    private TextView questionText;
    private RadioGroup answerGroup;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychology_survey);

        questionText = findViewById(R.id.questionText);
        answerGroup = findViewById(R.id.answerGroup);
        nextButton = findViewById(R.id.nextButton);

        showQuestion();

        nextButton.setOnClickListener(view -> {
            int selectedId = answerGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                return;
            }

            RadioButton selected = findViewById(selectedId);
            int score = Integer.parseInt(selected.getTag().toString());
            answers[currentQuestion] = score;

            currentQuestion++;

            if (currentQuestion < questions.length) {
                showQuestion();
            } else {
                // Anket bitti
                int totalScore = 0;
                for (int scoreVal : answers) totalScore += scoreVal;

                String result = totalScore < 20 ? "Rahat görünüyorsunuz 😊"
                        : totalScore < 30 ? "Biraz stresli görünüyorsunuz 😐"
                        : "Destek almayı düşünebilirsiniz 😟";

                Toast.makeText(this, "Anket tamamlandı. Sonuç: " + result, Toast.LENGTH_LONG).show();
                finish(); // İstersen burada yeni bir sonuç ekranına da geçebilirsin
            }
        });
    }

    private void showQuestion() {
        questionText.setText((currentQuestion + 1) + ". " + questions[currentQuestion]);
        answerGroup.clearCheck();
    }
}
