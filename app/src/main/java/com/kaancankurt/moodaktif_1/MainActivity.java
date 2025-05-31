package com.kaancankurt.moodaktif_1;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.kaancankurt.moodaktif_1.databinding.ActivityBottomnavbarBinding;

public class MainActivity extends AppCompatActivity {

    ActivityBottomnavbarBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Oturum kontrolü
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityBottomnavbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Drawer erişimi
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(binding.toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Drawer item seçimleri
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.nav_sleep_tracking:
                    startActivity(new Intent(this, SleepTrackingActivity.class));
                    break;
                case R.id.nav_read_books:
                    startActivity(new Intent(this, ReadBooksActivity.class));
                    break;
                case R.id.nav_daily_mood:
                    startActivity(new Intent(this, DailyMoodActivity.class));
                    break;
                case R.id.nav_activity_suggestions:
                    startActivity(new Intent(this, ActivitySuggestionsActivity.class));
                    break;
                case R.id.nav_music:
                    startActivity(new Intent(this, MusicActivity.class));
                    break;
                case R.id.nav_exercises:
                    startActivity(new Intent(this, ExercisesActivity.class));
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });

        // Varsayılan fragment
        if (savedInstanceState == null) {
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void performLogout() {
        sessionManager.logout();
        Intent intent = new Intent(this, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
