package com.example.chamikanandasiri.domesticlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BooksFragment extends Fragment {

    DataBaseHelper dataBaseHelper;
    View view;
    ListView Library_listView;
    EditText Library_searchText;
    Button Library_searchButton;
    ArrayList<String[]> bookDetails;

    private String TAG = "Test";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_books,container,false);
        Library_listView = view.findViewById(R.id.lstLibraryBooks);
        Library_searchText = view.findViewById(R.id.edtLibrarySearch);
        Library_searchButton = view.findViewById(R.id.btnLibrarySearch);
        Library_searchButton.setOnClickListener(this::SearchTitle);
        LoadAllBooks();
        return  view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getActivity());
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
        BookArrayAdapter adapter = new BookArrayAdapter(getActivity(), R.layout.listitem_book, bookDetails,dataBaseHelper);
        Library_listView.setAdapter(adapter);
    }


}
