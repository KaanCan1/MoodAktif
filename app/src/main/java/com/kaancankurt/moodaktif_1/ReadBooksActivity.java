package com.kaancankurt.moodaktif_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashSet;
import java.util.Set;

public class ReadBooksActivity extends AppCompatActivity {

    private EditText editBookName;
    private Button btnAddBook;
    private TextView txtBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_books);

        initializeViews();
        setupToolbar();
        setupClickListeners();
        loadBookList();
    }

    private void initializeViews() {
        editBookName = findViewById(R.id.editBookName);
        btnAddBook = findViewById(R.id.btnAddBook);
        txtBookList = findViewById(R.id.txtBookList);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Okunan Kitaplar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupClickListeners() {
        btnAddBook.setOnClickListener(v -> {
            String bookName = editBookName.getText().toString().trim();
            if (!bookName.isEmpty()) {
                addBook(bookName);
                editBookName.setText("");
                loadBookList();
                Toast.makeText(this, "Kitap eklendi: " + bookName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lütfen kitap adı girin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBook(String bookName) {
        Set<String> books = getSharedPreferences("books_data", MODE_PRIVATE)
                .getStringSet("book_list", new HashSet<>());
        books = new HashSet<>(books); // Create a new set to avoid modifying the original
        books.add(bookName);

        getSharedPreferences("books_data", MODE_PRIVATE)
                .edit()
                .putStringSet("book_list", books)
                .apply();
    }

    private void loadBookList() {
        Set<String> books = getSharedPreferences("books_data", MODE_PRIVATE)
                .getStringSet("book_list", new HashSet<>());

        if (books.isEmpty()) {
            txtBookList.setText("Henüz kitap eklemediniz.");
        } else {
            StringBuilder bookListText = new StringBuilder("Okuduğunuz Kitaplar:\n\n");
            int count = 1;
            for (String book : books) {
                bookListText.append(count).append(". ").append(book).append("\n");
                count++;
            }
            txtBookList.setText(bookListText.toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}