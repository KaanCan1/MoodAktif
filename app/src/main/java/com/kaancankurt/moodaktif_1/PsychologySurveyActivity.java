package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PsychologySurveyActivity extends AppCompatActivity {

    private List<SurveyQuestion> questions = new ArrayList<>();
    private int currentQuestion = 0;
    private Map<String, String> answers = new HashMap<>();

    private TextView questionText;
    private TextView questionCounter;
    private RadioGroup answerGroup;
    private Button nextButton;
    private Button previousButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychology_survey);

        initializeViews();
        initializeQuestions();
        showQuestion();

        nextButton.setOnClickListener(view -> {
            if (saveCurrentAnswer()) {
                currentQuestion++;
                showQuestion();
            }
        });

        previousButton.setOnClickListener(view -> {
            if (currentQuestion > 0) {
                currentQuestion--;
                showQuestion();
            }
        });

        submitButton.setOnClickListener(view -> {
            if (saveCurrentAnswer()) {
                generateRecommendations();
            }
        });
    }

    private void initializeViews() {
        questionText = findViewById(R.id.questionText);
        questionCounter = findViewById(R.id.questionCounter);
        answerGroup = findViewById(R.id.answerGroup);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        submitButton = findViewById(R.id.submitButton);
    }

    private void initializeQuestions() {
        questions.add(new SurveyQuestion(
                "Güncel ruh haliniz nedir?",
                new String[]{"Mutlu", "Mutsuz", "Endişeli", "Stresli", "Diğer"},
                new String[]{"mutlu", "mutsuz", "endiseli", "stresli", "diger"}
        ));

        questions.add(new SurveyQuestion(
                "Yaş aralığınız nedir?",
                new String[]{"18-25", "25 ve üzeri"},
                new String[]{"18-25", "25_ve_uzeri"}
        ));

        questions.add(new SurveyQuestion(
                "Daha önce psikolojik destek ya da ilaç kullandınız mı?",
                new String[]{"Evet", "Hayır", "Sadece psikolojik destek aldım"},
                new String[]{"evet", "hayir", "sadece_destek_aldim"}
        ));

        questions.add(new SurveyQuestion(
                "Kronik fiziksel ve ya zihinsel rahatsızlığınız var mı?",
                new String[]{"Evet", "Hayır"},
                new String[]{"evet", "hayir"}
        ));

        questions.add(new SurveyQuestion(
                "Yalnız mı yaşıyorsunuz yoksa arkadaşlarınızla mı kalıyorsunuz?",
                new String[]{"Yalnız", "Arkadaşlarla / eş ile", "Aileyle"},
                new String[]{"yalniz", "arkadaslarla", "aileyle"}
        ));

        questions.add(new SurveyQuestion(
                "Sosyal olarak kendinizi nasıl tanımlarsınız?",
                new String[]{"İçe dönük", "Dışa dönük"},
                new String[]{"ice_donuk", "disa_donuk"}
        ));

        questions.add(new SurveyQuestion(
                "Ailenizi ne sıklıkla görüyörsunuz?",
                new String[]{"Hiç", "Her zaman", "Bazen"},
                new String[]{"hic", "her_zamana", "bazen"}
        ));

        questions.add(new SurveyQuestion(
                "Günlük ortalama ne kadar boş zamanınız var?",
                new String[]{"1 saatten az", "1-2 saat", "2-4 saat"},
                new String[]{"1_saatten_az", "1-2_saat", "2-4_saat"}
        ));
    }

    private void showQuestion() {
        SurveyQuestion question = questions.get(currentQuestion);

        questionCounter.setText("Soru " + (currentQuestion + 1) + "/" + questions.size());
        questionText.setText(question.getQuestion());

        // RadioButtons'ları güncelle
        String[] options = question.getOptions();
        String[] values = question.getValues();

        answerGroup.clearCheck();

        // Mevcut cevabı kontrol et
        String currentAnswer = answers.get("q" + currentQuestion);

        for (int i = 0; i < 5; i++) {
            RadioButton radioButton = (RadioButton) answerGroup.getChildAt(i);
            if (i < options.length) {
                radioButton.setText(options[i]);
                radioButton.setTag(values[i]);
                radioButton.setVisibility(View.VISIBLE);

                // Önceki cevabı seç
                if (values[i].equals(currentAnswer)) {
                    radioButton.setChecked(true);
                }
            } else {
                radioButton.setVisibility(View.GONE);
            }
        }

        // Buton görünürlüğünü ayarla
        previousButton.setVisibility(currentQuestion > 0 ? View.VISIBLE : View.GONE);
        nextButton.setVisibility(currentQuestion < questions.size() - 1 ? View.VISIBLE : View.GONE);
        submitButton.setVisibility(currentQuestion == questions.size() - 1 ? View.VISIBLE : View.GONE);
    }

    private boolean saveCurrentAnswer() {
        int selectedId = answerGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Lütfen bir seçenek seçin", Toast.LENGTH_SHORT).show();
            return false;
        }

        RadioButton selected = findViewById(selectedId);
        String answer = selected.getTag().toString();
        answers.put("q" + currentQuestion, answer);
        return true;
    }

    private void generateRecommendations() {
        // Cevapları uygun format için hazırla
        Map<String, String> surveyData = new HashMap<>();
        surveyData.put("mood", answers.get("q0"));
        surveyData.put("age", answers.get("q1"));
        surveyData.put("medication", answers.get("q2"));
        surveyData.put("health", answers.get("q3"));
        surveyData.put("living", answers.get("q4"));
        surveyData.put("social_interaction", answers.get("q5"));
        surveyData.put("family_visits", answers.get("q6"));
        surveyData.put("free_time", answers.get("q7"));

        // Öneri oluşturma sistemi
        RecommendationEngine engine = new RecommendationEngine();
        List<Recommendation> recommendations = engine.generateRecommendations(surveyData);

        // Sonuç sayfasına git
        Intent intent = new Intent(this, RecommendationResultActivity.class);
        intent.putExtra("survey_data", (HashMap<String, String>) surveyData);
        intent.putParcelableArrayListExtra("recommendations", (ArrayList<Recommendation>) recommendations);
        startActivity(intent);
        finish();
    }

    private static class SurveyQuestion {
        private String question;
        private String[] options;
        private String[] values;

        public SurveyQuestion(String question, String[] options, String[] values) {
            this.question = question;
            this.options = options;
            this.values = values;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public String[] getValues() {
            return values;
        }
    }
}
