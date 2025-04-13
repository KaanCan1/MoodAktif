package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class ProfileFragment extends Fragment {

    // İsteğe bağlı parametreler (varsa)
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Boş public constructor gerekli
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // fragment_profile.xml layoutunu inflate ediyoruz
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // XML layout içerisindeki view'ları referans alıyoruz
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        // Toolbar referansı Activity'nin ActionBar'ı olarak ayarlanır.
        // Bu işlemin çalışması için Activity'nin AppCompatActivity olduğundan emin olun.
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        // ActionBarDrawerToggle ile hamburger menüyü etkinleştiriyoruz
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView üzerindeki menü öğesi seçimlerini dinliyoruz
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Menü öğesi seçildiğinde ilgili işlemleri burada yapabilirsiniz
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_profile:
                        // Profil seçildiğinde yapılacak işlemler
                        break;
                    case R.id.nav_sleep_tracking:
                        // Uyku takip işlemleri
                        break;
                    case R.id.nav_read_books:
                        // Okunan kitaplar işlemleri
                        break;
                    case R.id.nav_daily_mood:
                        // Günlük mod işlemleri
                        break;
                    case R.id.nav_activity_suggestions:
                        // Aktivite önerileri işlemleri
                        break;
                    case R.id.nav_music:
                        // Müzik işlemleri
                        break;
                    case R.id.nav_exercises:
                        // Egzersiz işlemleri
                        break;
                }
                // Menü seçiminden sonra drawer'ı kapatıyoruz
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
    }
}
