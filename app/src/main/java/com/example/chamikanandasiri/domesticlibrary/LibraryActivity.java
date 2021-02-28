package com.example.chamikanandasiri.domesticlibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class LibraryActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    ListView Library_listView;
    EditText Library_searchText;
    Button Library_searchButton;
    ArrayList<String[]> bookDetails;

    private String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        dataBaseHelper = new DataBaseHelper(this);
        Library_listView = findViewById(R.id.lstLibraryBooks);
        Library_searchText = findViewById(R.id.edtLibrarySearch);
        Library_searchButton = findViewById(R.id.btnLibrarySearch);
        Library_searchButton.setOnClickListener(this::SearchTitle);
        LoadAllBooks();
    }

    public void SearchTitle(View v1) {
        String q = Library_searchText.getText().toString();
        if (!q.equals("") && q.length() > 1) {
            bookDetails = dataBaseHelper.getSimilarBookDetails(q);
            bookListGenerate();
        } else {
            LoadAllBooks();
        }
    }

    private void LoadAllBooks() {
        bookDetails = dataBaseHelper.getAllBookDetails();
        bookListGenerate();
    }


    private void bookListGenerate() {
        BookArrayAdapter adapter = new BookArrayAdapter(this, R.layout.listitem_book, bookDetails);
        Library_listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

