package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setupToolbar();
        setupFaqItems();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupFaqItems() {
        setupFaqItem(
                findViewById(R.id.faq_item_1),
                findViewById(R.id.answer_1),
                findViewById(R.id.arrow_1)
        );

        setupFaqItem(
                findViewById(R.id.faq_item_2),
                findViewById(R.id.answer_2),
                findViewById(R.id.arrow_2)
        );

        setupFaqItem(
                findViewById(R.id.faq_item_3),
                findViewById(R.id.answer_3),
                findViewById(R.id.arrow_3)
        );

        setupFaqItem(
                findViewById(R.id.faq_item_4),
                findViewById(R.id.answer_4),
                findViewById(R.id.arrow_4)
        );

        setupFaqItem(
                findViewById(R.id.faq_item_5),
                findViewById(R.id.answer_5),
                findViewById(R.id.arrow_5)
        );
    }

    private void setupFaqItem(LinearLayout faqItem, TextView answer, ImageView arrow) {
        faqItem.setOnClickListener(v -> {
            if (answer.getVisibility() == View.GONE) {
                // Expand
                answer.setVisibility(View.VISIBLE);
                rotateArrow(arrow, 0f, 180f);
            } else {
                // Collapse
                answer.setVisibility(View.GONE);
                rotateArrow(arrow, 180f, 0f);
            }
        });
    }

    private void rotateArrow(ImageView arrow, float fromDegree, float toDegree) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegree, toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        arrow.startAnimation(rotateAnimation);
    }
}
