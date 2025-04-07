package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.app.AppCompatActivity;

public class FaqActivity extends AppCompatActivity {

    private String[] questions = {
            "MoodAktif uygulamasını nasıl kullanırım?",
            "MoodAktif'e nasıl kayıt olurum?",
            "MoodAktif nedir?",
            "Psikolojik analizler neye göre yapılıyor?",
            "Verilerim güvende mi?",
            "MoodAktif anketi ne işe yarar?"
    };

    private String[] answers = {
            "Uygulamayı açtıktan sonra ruh halinize göre anketi doldurabilir, önerileri görüntüleyebilirsiniz.",
            "Ana ekranda 'Kayıt Ol' seçeneğine tıklayarak hesap oluşturabilirsiniz.",
            "MoodAktif, ruh halinize göre öneriler sunan psikolojik analiz uygulamasıdır.",
            "Anket cevaplarınıza göre öneri sistemimiz çalışır, verileriniz işlenir.",
            "Evet, verileriniz gizlilik politikası kapsamında korunur.",
            "Anket, ruh halinizi analiz ederek size uygun öneriler sunar."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sıkça Sorulan Sorular");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(32, 32, 32, 32);

        for (int i = 0; i < questions.length; i++) {
            View qaView = createFaqItem(this, questions[i], answers[i]);
            container.addView(qaView);
        }

        scrollView.addView(container);
        setContentView(scrollView);
    }

    private View createFaqItem(Context context, String question, String answer) {
        LinearLayout faqLayout = new LinearLayout(context);
        faqLayout.setOrientation(LinearLayout.VERTICAL);
        faqLayout.setPadding(40, 40, 40, 40);

        // Hafif köşeli, gri arka plan
        GradientDrawable background = new GradientDrawable();
        background.setCornerRadius(24);
        background.setColor(0xFFF5F5F5); // Açık gri
        faqLayout.setBackground(background);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 32);
        faqLayout.setLayoutParams(layoutParams);

        // Soru metni + ok emoji
        TextView questionText = new TextView(context);
        questionText.setText("➕ " + question); // Başlangıçta + olarak gösteriyoruz
        questionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        questionText.setTypeface(null, Typeface.BOLD);
        questionText.setTextColor(0xFF000000);

        // Cevap metni
        final TextView answerText = new TextView(context);
        answerText.setText(answer);
        answerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        answerText.setPadding(0, 16, 0, 0);
        answerText.setTextColor(0xFF444444);
        answerText.setVisibility(View.GONE);

        faqLayout.setClickable(true);
        faqLayout.setFocusable(true);

        // Tıklama ile aç/kapa + ok yönünü değiştirme
        faqLayout.setOnClickListener(v -> {
            if (answerText.getVisibility() == View.GONE) {
                answerText.setVisibility(View.VISIBLE);
                questionText.setText("➖ " + question); // Açıldıysa eksi
            } else {
                answerText.setVisibility(View.GONE);
                questionText.setText("➕ " + question); // Kapalıysa artı
            }
        });

        faqLayout.addView(questionText);
        faqLayout.addView(answerText);

        return faqLayout;
    }
}
