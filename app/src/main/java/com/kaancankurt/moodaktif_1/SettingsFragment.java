package com.kaancankurt.moodaktif_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Boş yapıcı metod (gerekli)
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // layout'u bağla
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Butonları tanımla
        Button btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        Button btnHelpFaq = view.findViewById(R.id.btn_help_faq);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        // Profil Güncelleme Butonu
        btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfilGuncelleActivity.class);
            startActivity(intent);
        });


        // SSS (FAQ) Butonu
        btnHelpFaq.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FaqActivity.class);
            startActivity(intent);
        });

        // Çıkış Yap Butonu
        btnLogout.setOnClickListener(v -> {


            // Eğer oturum varsa, buraya çıkış işlemi (örneğin Firebase) eklenebilir
            // FirebaseAuth.getInstance().signOut();

            // Uygulamayı tamamen kapat
            requireActivity().finishAffinity(); // Tüm aktiviteleri sonlandırır
        });


        return view;
    }
}
