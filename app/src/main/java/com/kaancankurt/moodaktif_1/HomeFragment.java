package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView welcomeText, dailyTipText;
    private ImageView profileImageView;
    private AppCompatButton startSurveyButton, startBreathingBtn, startMeditationBtn;
    private LinearLayout moodHappy, moodNeutral, moodSad, moodStressed;
    private CardView moodCheckCard, quickMoodCard, videoCard, dailyTipsCard;
    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;

    // SharedPreferences key for profile image
    private static final String PREF_PROFILE_IMAGE = "profile_image_";

    // Günlük ipuçları dizisi
    private String[] dailyTips = {
            "Derin nefes almak, zihinsel stresi azaltmanın en etkili yollarından biridir. Bugün 5 dakikanızı derin nefes egzersizlerine ayırın.",
            "Günde en az 10 dakika meditasyon yapmak, zihinsel netliği artırır ve stresi azaltır.",
            "Doğada vakit geçirmek, serotonin seviyesini yükseltir ve ruh halinizi iyileştirir.",
            "Günlük tutmak, duygularınızı daha iyi anlamanıza ve yönetmenize yardımcı olur.",
            "Uyku kalitesi, zihinsel sağlığınızı doğrudan etkiler. Düzenli uyku saatlerine dikkat edin.",
            "Sevdiklerinizle vakit geçirmek, oxytocin hormonunu artırır ve mutluluk hissi verir.",
            "Küçük başarılarınızı kutlamak, motivasyonunuzu artırır ve pozitif düşünceyi güçlendirir."
    };

    // YouTube video URL'leri
    private static final String BREATHING_VIDEO_URL = "https://www.youtube.com/watch?v=gL3jEs8aF6w";
    private static final String MEDITATION_VIDEO_URL = "https://www.youtube.com/watch?v=--1sTncKu2w";

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        initializeData();
        setupClickListeners();
        loadRandomTip();
        startAnimations();

        return view;
    }

    private void initializeViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        dailyTipText = view.findViewById(R.id.dailyTipText);
        profileImageView = view.findViewById(R.id.profileImageView);
        startSurveyButton = view.findViewById(R.id.startSurveyButton);
        startBreathingBtn = view.findViewById(R.id.startBreathingBtn);
        startMeditationBtn = view.findViewById(R.id.startMeditationBtn);

        // Card views
        moodCheckCard = view.findViewById(R.id.moodCheckCard);
        quickMoodCard = view.findViewById(R.id.quickMoodCard);
        videoCard = view.findViewById(R.id.videoCard);
        dailyTipsCard = view.findViewById(R.id.dailyTipsCard);

        // Mood selection views
        moodHappy = view.findViewById(R.id.mood_happy);
        moodNeutral = view.findViewById(R.id.mood_neutral);
        moodSad = view.findViewById(R.id.mood_sad);
        moodStressed = view.findViewById(R.id.mood_stressed);
    }

    private void initializeData() {
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("ProfilePrefs", getContext().MODE_PRIVATE);

        // Kullanıcı ismini göster
        if (sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            if (!userName.isEmpty()) {
                welcomeText.setText("Hoş geldiniz, " + userName.split(" ")[0] + "!");
            }
        }

        // Profil resmini yükle
        loadProfileImage();
    }

    private void loadProfileImage() {
        try {
            int userId = sessionManager.getUserId();
            String key = PREF_PROFILE_IMAGE + userId;

            String encodedImage = sharedPreferences.getString(key, null);
            if (encodedImage != null) {
                // Base64 string'i Bitmap'e çevir
                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bitmap != null) {
                    profileImageView.setImageBitmap(bitmap);
                    // Tint'i kaldır çünkü artık gerçek resim var
                    profileImageView.clearColorFilter();
                }
            }
        } catch (Exception e) {
            // Hata durumunda varsayılan resmi göster
            profileImageView.setImageResource(R.drawable.baseline_person_24);
            e.printStackTrace();
        }
    }

    private void startAnimations() {
        // Kartlara animasyon ekle
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        // Animasyonları sırayla başlat
        moodCheckCard.startAnimation(fadeIn);

        quickMoodCard.postDelayed(() -> {
            if (quickMoodCard != null) quickMoodCard.startAnimation(slideUp);
        }, 200);

        videoCard.postDelayed(() -> {
            if (videoCard != null) videoCard.startAnimation(fadeIn);
        }, 400);

        dailyTipsCard.postDelayed(() -> {
            if (dailyTipsCard != null) dailyTipsCard.startAnimation(slideUp);
        }, 600);

        // Mood butonlarına hover efekti ekle
        addMoodButtonAnimation(moodHappy);
        addMoodButtonAnimation(moodNeutral);
        addMoodButtonAnimation(moodSad);
        addMoodButtonAnimation(moodStressed);
    }

    private void addMoodButtonAnimation(LinearLayout moodButton) {
        moodButton.setOnClickListener(v -> {
            Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            v.startAnimation(bounce);

            // Orijinal click handler'ı çağır
            String moodText = "";
            if (v == moodHappy) moodText = "Mutlu 😊";
            else if (v == moodNeutral) moodText = "Normal 😐";
            else if (v == moodSad) moodText = "Üzgün 😢";
            else if (v == moodStressed) moodText = "Stresli 😰";

            // Final variable for lambda
            final String finalMoodText = moodText;

            // Animasyon bittikten sonra mood selection'ı handle et
            v.postDelayed(() -> handleMoodSelection(finalMoodText), 300);
        });
    }

    private void setupClickListeners() {
        // Psikoloji testi butonu
        startSurveyButton.setOnClickListener(v -> {
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            v.startAnimation(scaleUp);

            v.postDelayed(() -> {
                Intent intent = new Intent(getActivity(), PsychologySurveyActivity.class);
                startActivity(intent);
            }, 150);
        });

        // Video butonları - YouTube'a yönlendir
        startBreathingBtn.setOnClickListener(v -> {
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            v.startAnimation(scaleUp);

            v.postDelayed(() -> {
                openYouTubeVideo(BREATHING_VIDEO_URL);
            }, 150);
        });

        startMeditationBtn.setOnClickListener(v -> {
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            v.startAnimation(scaleUp);

            v.postDelayed(() -> {
                openYouTubeVideo(MEDITATION_VIDEO_URL);
            }, 150);
        });
    }

    private void openYouTubeVideo(String videoUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        } catch (Exception e) {
            // YouTube uygulaması yoksa web tarayıcısında aç
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);
        }
    }

    private void handleMoodSelection(String mood) {
        Toast.makeText(getContext(), "Ruh halinizi kaydettik: " + mood, Toast.LENGTH_SHORT).show();

        // Ruh haline göre özel mesajlar
        String message = "";
        switch (mood) {
            case "Mutlu 😊":
                message = "Harika! Bu pozitif enerjiyi koruyun. 🌟";
                break;
            case "Normal 😐":
                message = "Dengeli bir gün! Küçük aktivitelerle ruh halinizi yükseltebilirsiniz. ⚖️";
                break;
            case "Üzgün 😢":
                message = "Bu hisler geçicidir. Kendinize nazik olun ve destek almaktan çekinmeyin. 💙";
                break;
            case "Stresli 😰":
                message = "Derin nefes alın. Stres yönetimi için meditasyon önerileri deneyebilirsiniz. 🌸";
                break;
        }

        // İkinci mesajı gecikmeli göster
        final String finalMessage = message;
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), finalMessage, Toast.LENGTH_LONG).show();
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadRandomTip() {
        Random random = new Random();
        int randomIndex = random.nextInt(dailyTips.length);
        dailyTipText.setText("\"" + dailyTips[randomIndex] + "\"");

        // Tip metnine animasyon ekle
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        dailyTipText.startAnimation(fadeIn);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragment'a geri döndüğünde yeni ipucu ve profil resmini yükle
        loadRandomTip();
        loadProfileImage();
    }
}
