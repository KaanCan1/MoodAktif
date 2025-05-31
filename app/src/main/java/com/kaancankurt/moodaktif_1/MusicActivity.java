package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MusicActivity extends AppCompatActivity {

    private RadioGroup radioGroupMusicMood;
    private Button btnGetMusicRecommendations;
    private TextView txtMusicRecommendations;
    private Button btnPopGenre, btnRockGenre, btnClassicalGenre, btnJazzGenre, btnElectronicGenre;
    private Button btnNatureSounds, btnMeditationMusic, btnAmbientMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initializeViews();
        setupToolbar();
        setupClickListeners();
    }

    private void initializeViews() {
        radioGroupMusicMood = findViewById(R.id.radioGroupMusicMood);
        btnGetMusicRecommendations = findViewById(R.id.btnGetMusicRecommendations);
        txtMusicRecommendations = findViewById(R.id.txtMusicRecommendations);

        // Genre buttons
        btnPopGenre = findViewById(R.id.btnPopGenre);
        btnRockGenre = findViewById(R.id.btnRockGenre);
        btnClassicalGenre = findViewById(R.id.btnClassicalGenre);
        btnJazzGenre = findViewById(R.id.btnJazzGenre);
        btnElectronicGenre = findViewById(R.id.btnElectronicGenre);

        // Relaxation buttons
        btnNatureSounds = findViewById(R.id.btnNatureSounds);
        btnMeditationMusic = findViewById(R.id.btnMeditationMusic);
        btnAmbientMusic = findViewById(R.id.btnAmbientMusic);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Müzik");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupClickListeners() {
        btnGetMusicRecommendations.setOnClickListener(v -> {
            int selectedId = radioGroupMusicMood.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedMood = findViewById(selectedId);
                String recommendations = getMusicRecommendationsForMood(selectedMood.getId());
                txtMusicRecommendations.setText(recommendations);
            } else {
                Toast.makeText(this, "Lütfen bir ruh hali seçin", Toast.LENGTH_SHORT).show();
            }
        });

        // Genre button listeners
        btnPopGenre.setOnClickListener(v -> showGenreInfo("Pop Müzik",
                "• Dua Lipa - Levitating\n• Ed Sheeran - Shape of You\n• Taylor Swift - Anti-Hero\n• Harry Styles - As It Was\n• Billie Eilish - Bad Guy"));

        btnRockGenre.setOnClickListener(v -> showGenreInfo("Rock Müzik",
                "• Queen - Bohemian Rhapsody\n• Led Zeppelin - Stairway to Heaven\n• AC/DC - Thunderstruck\n• Guns N' Roses - Sweet Child O' Mine\n• The Beatles - Come Together"));

        btnClassicalGenre.setOnClickListener(v -> showGenreInfo("Klasik Müzik",
                "• Beethoven - 9. Senfoni\n• Mozart - Eine kleine Nachtmusik\n• Bach - Brandenburg Concertos\n• Chopin - Nocturne Op. 9 No. 2\n• Vivaldi - Four Seasons"));

        btnJazzGenre.setOnClickListener(v -> showGenreInfo("Jazz",
                "• Miles Davis - Kind of Blue\n• John Coltrane - A Love Supreme\n• Duke Ellington - Take Five\n• Ella Fitzgerald - Summertime\n• Louis Armstrong - What a Wonderful World"));

        btnElectronicGenre.setOnClickListener(v -> showGenreInfo("Elektronik Müzik",
                "• Daft Punk - One More Time\n• Calvin Harris - Feel So Close\n• Deadmau5 - Ghosts 'n' Stuff\n• Avicii - Levels\n• Swedish House Mafia - Don't You Worry Child"));

        // Relaxation button listeners
        btnNatureSounds.setOnClickListener(v -> showGenreInfo("Doğa Sesleri",
                "• Yağmur sesi ve gök gürültüsü\n• Orman kuşları ve yaprak hışırtısı\n• Okyanus dalgaları\n• Akarsu ve şelale sesleri\n• Rüzgar ve ağaç sesleri"));

        btnMeditationMusic.setOnClickListener(v -> showGenreInfo("Meditasyon Müziği",
                "• Tibetan Singing Bowls\n• Om Chanting\n• Peaceful Piano\n• Zen Garden\n• Chakra Healing Sounds"));

        btnAmbientMusic.setOnClickListener(v -> showGenreInfo("Ambient Müzik",
                "• Brian Eno - Music for Airports\n• Max Richter - Sleep\n• Ólafur Arnalds - Near Light\n• Nils Frahm - Says\n• Tim Hecker - Harmony in Ultraviolet"));
    }

    private String getMusicRecommendationsForMood(int moodId) {
        if (moodId == R.id.radioHappyMusic) {
            return "😊 Mutlu Ruh Hali İçin Öneriler:\n\n" +
                    "🎵 Şarkı Önerileri:\n" +
                    "• Pharrell Williams - Happy\n" +
                    "• Justin Timberlake - Can't Stop the Feeling\n" +
                    "• Bruno Mars - Uptown Funk\n" +
                    "• Mark Ronson - Uptown Funk\n" +
                    "• Katrina & The Waves - Walking on Sunshine\n\n" +
                    "🎼 Türler: Pop, Dance, Funk, Soul";

        } else if (moodId == R.id.radioSadMusic) {
            return "😢 Üzgün Ruh Hali İçin Öneriler:\n\n" +
                    "🎵 Şarkı Önerileri:\n" +
                    "• Johnny Cash - Hurt\n" +
                    "• Mad World - Gary Jules\n" +
                    "• The Sound of Silence - Simon & Garfunkel\n" +
                    "• Everybody Hurts - R.E.M.\n" +
                    "• Black - Pearl Jam\n\n" +
                    "🎼 Türler: Alternative, Indie, Acoustic, Folk";

        } else if (moodId == R.id.radioStressedMusic) {
            return "😰 Stresli Ruh Hali İçin Öneriler:\n\n" +
                    "🎵 Şarkı Önerileri:\n" +
                    "• Weightless - Marconi Union\n" +
                    "• Clair de Lune - Debussy\n" +
                    "• Gymnopédie No. 1 - Erik Satie\n" +
                    "• River Flows in You - Yiruma\n" +
                    "• Spiegel im Spiegel - Arvo Pärt\n\n" +
                    "🎼 Türler: Classical, Ambient, New Age, Instrumental";

        } else if (moodId == R.id.radioEnergeticMusic) {
            return "⚡ Enerjik Ruh Hali İçin Öneriler:\n\n" +
                    "🎵 Şarkı Önerileri:\n" +
                    "• Eye of the Tiger - Survivor\n" +
                    "• Don't Stop Me Now - Queen\n" +
                    "• Titanium - David Guetta ft. Sia\n" +
                    "• Stronger - Kanye West\n" +
                    "• Pump It - Black Eyed Peas\n\n" +
                    "🎼 Türler: Rock, Electronic, Hip-Hop, Dance";

        } else if (moodId == R.id.radioRelaxedMusic) {
            return "🧘‍♀️ Sakin Ruh Hali İçin Öneriler:\n\n" +
                    "🎵 Şarkı Önerileri:\n" +
                    "• Breathe Me - Sia\n" +
                    "• Mad About You - Sting\n" +
                    "• The Night We Met - Lord Huron\n" +
                    "• Holocene - Bon Iver\n" +
                    "• Iron & Wine - Boy with a Coin\n\n" +
                    "🎼 Türler: Indie, Folk, Jazz, Lounge";
        }

        return "Lütfen bir ruh hali seçin.";
    }

    private void showGenreInfo(String genreTitle, String content) {
        txtMusicRecommendations.setText("🎵 " + genreTitle + " Önerileri:\n\n" + content);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
