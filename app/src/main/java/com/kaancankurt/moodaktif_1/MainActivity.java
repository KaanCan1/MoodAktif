package com.kaancankurt.moodaktif_1;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.kaancankurt.moodaktif_1.databinding.ActivityBottomnavbarBinding;

public class MainActivity extends AppCompatActivity {

    ActivityBottomnavbarBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    //replaceFragment(new SleepFragment());
                    break;
                case R.id.nav_read_books:
                    //replaceFragment(new BooksFragment());
                    break;
                case R.id.nav_daily_mood:
                    //replaceFragment(new DailyMoodFragment());
                    break;
                case R.id.nav_activity_suggestions:
                    //replaceFragment(new ActivitySuggestionsFragment());
                    break;
                case R.id.nav_music:
                    //replaceFragment(new MusicFragment());
                    break;
                case R.id.nav_exercises:
                    //replaceFragment(new ExercisesFragment());
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
}
