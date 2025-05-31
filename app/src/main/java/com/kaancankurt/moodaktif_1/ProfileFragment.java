package com.kaancankurt.moodaktif_1;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private TextView txtUserName;
    private ImageView imgProfile, imgEditProfile;
    private Button btnSleepTracking, btnReadBooks, btnDailyMood, btnActivitySuggestions,
            btnMusic, btnExercises, btnUpdateProfile;
    private SessionManager sessionManager;
    private AppDatabase database;
    private ExecutorService executorService;
    private SharedPreferences sharedPreferences;

    // Activity Result Launchers for image selection
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    // SharedPreferences key for profile image
    private static final String PREF_PROFILE_IMAGE = "profile_image_";

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
        txtUserName = view.findViewById(R.id.txtUserName);
        imgProfile = view.findViewById(R.id.imgProfile);
        imgEditProfile = view.findViewById(R.id.imgEditProfile);

        btnSleepTracking = view.findViewById(R.id.btnSleepTracking);
        btnReadBooks = view.findViewById(R.id.btnReadBooks);
        btnDailyMood = view.findViewById(R.id.btnDailyMood);
        btnActivitySuggestions = view.findViewById(R.id.btnActivitySuggestions);
        btnMusic = view.findViewById(R.id.btnMusic);
        btnExercises = view.findViewById(R.id.btnExercises);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);

        // Toolbar'ı set ediyoruz (ActionBar gibi kullanmak için)
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Profil");  // Toolbar başlığını ayarlıyoruz

        initializeDatabase();
        setupActivityResultLaunchers();
        loadUserData();
        loadProfileImage(); // Kayıtlı profil resmini yükle
        setupClickListeners();
    }

    private void initializeDatabase() {
        database = AppDatabase.getDatabase(getContext());
        executorService = Executors.newSingleThreadExecutor();
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("ProfilePrefs", getContext().MODE_PRIVATE);
    }

    private void setupActivityResultLaunchers() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            if (imageBitmap != null) {
                                imgProfile.setImageBitmap(imageBitmap);
                                saveProfileImageToDatabase(imageBitmap);
                                Toast.makeText(getContext(), "📸 Profil resmi kameradan güncellendi!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        // Gallery launcher - hem galeri hem de dosya seçici için
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImageUri);
                                Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream);
                                if (selectedBitmap != null) {
                                    imgProfile.setImageBitmap(selectedBitmap);
                                    saveProfileImageToDatabase(selectedBitmap);
                                    Toast.makeText(getContext(), "🖼️ Profil resmi başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "❌ Resim formatı desteklenmiyor", Toast.LENGTH_SHORT).show();
                                }
                            } catch (FileNotFoundException e) {
                                Toast.makeText(getContext(), "❌ Seçilen dosyaya erişilemedi", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "❌ Resim yüklenirken hata oluştu", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        // Permission launcher
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        showImagePickerDialog();
                    } else {
                        Toast.makeText(getContext(), "⚠️ Kamera izni gerekli", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void saveProfileImageToDatabase(Bitmap bitmap) {
        // Bitmap'i Base64 string'e çevir ve SharedPreferences'e kaydet
        try {
            int userId = sessionManager.getUserId();
            String key = PREF_PROFILE_IMAGE + userId;

            // Bitmap'i sıkıştır ve Base64'e çevir
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // Kaliteyi %70 olarak ayarla (dosya boyutunu küçültmek için)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // SharedPreferences'e kaydet
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, encoded);
            editor.apply();

        } catch (Exception e) {
            Toast.makeText(getContext(), "❌ Profil resmi kaydedilirken hata oluştu", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("📷 Profil Resmi Nasıl Seçmek İstersiniz?");
        builder.setItems(new String[]{"🖼️ Galeri", "📁 Dosyalarım", "📸 Kamera"}, (dialog, which) -> {
            if (which == 0) {
                // Galeri seçildi
                openGallery();
            } else if (which == 1) {
                // Dosya seçici
                openDocumentPicker();
            } else {
                // Kamera seçildi
                openCamera();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(getContext(), "Kamera uygulaması bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

    private void openDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "image/jpg", "image/webp"});
        galleryLauncher.launch(intent);
    }

    private void checkCameraPermissionAndProceed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(android.Manifest.permission.CAMERA);
            } else {
                showImagePickerDialog();
            }
        } else {
            showImagePickerDialog();
        }
    }

    private void loadUserData() {
        if (!sessionManager.isLoggedIn()) {
            return;
        }

        String userName = sessionManager.getUserName();
        if (!userName.isEmpty()) {
            txtUserName.setText(userName);
        }

        // Veritabanından güncel kullanıcı bilgilerini yükle
        int userId = sessionManager.getUserId();
        executorService.execute(() -> {
            try {
                User user = database.userDao().getUserById(userId);
                if (user != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        txtUserName.setText(user.getName());
                        // Diğer profil bilgilerini de güncelleyebiliriz
                    });
                }
            } catch (Exception e) {
                // Hata durumunda session'dan gelen bilgiyi kullan
            }
        });
    }

    private void setupClickListeners() {
        // Profil resmi düzenleme tıklama olayı
        imgEditProfile.setOnClickListener(v -> checkCameraPermissionAndProceed());

        // Profil resminin kendisine de tıklama olayı ekle
        imgProfile.setOnClickListener(v -> checkCameraPermissionAndProceed());

        btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfilGuncelleActivity.class);
            startActivity(intent);
        });

        btnSleepTracking.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SleepTrackingActivity.class);
            startActivity(intent);
        });

        btnReadBooks.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReadBooksActivity.class);
            startActivity(intent);
        });

        btnDailyMood.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DailyMoodActivity.class);
            startActivity(intent);
        });

        btnActivitySuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivitySuggestionsActivity.class);
            startActivity(intent);
        });

        btnMusic.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MusicActivity.class);
            startActivity(intent);
        });

        btnExercises.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExercisesActivity.class);
            startActivity(intent);
        });
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
                    imgProfile.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            // Hata durumunda varsayılan resmi göster
            imgProfile.setImageResource(R.drawable.baseline_person_24);
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragment'a geri döndüğünde kullanıcı bilgilerini ve profil resmini yenile
        loadUserData();
        loadProfileImage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
