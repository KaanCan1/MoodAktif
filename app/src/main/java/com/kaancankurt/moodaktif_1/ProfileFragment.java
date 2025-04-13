package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Boş public constructor gerekli
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
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        // Toolbar'ı set ediyoruz (ActionBar gibi kullanmak için)
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Profil");  // Toolbar başlığını ayarlıyoruz
    }
}
