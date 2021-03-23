package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BooksFragment extends Fragment {

    DataBaseHelper dataBaseHelper;
    View view;
    EditText Library_searchText;
    ImageButton Library_searchButton, Library_resetButton;
    ArrayList<String[]> bookDetails;
    RecyclerView Books_recyclerView;
    BookViewAdapter bookViewAdapter;

    private String TAG = "Test";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_books, container, false);
        Books_recyclerView = view.findViewById(R.id.recyclerViewBook);
        Library_searchText = view.findViewById(R.id.edtLibrarySearch);
        Library_searchButton = view.findViewById(R.id.btnLibrarySearch);
        Library_resetButton = view.findViewById(R.id.btnLibraryReset);
        Library_searchButton.setOnClickListener(this::SearchTitle);
        Library_resetButton.setOnClickListener(view1 -> {
            LoadAllBooks();
            Library_searchText.setText("");
            Library_resetButton.setVisibility(View.GONE);
        });
        LoadAllBooks();
        Books_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                ((StorageActivity) Objects.requireNonNull(getActivity())).controlFABVisibility(dy);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getActivity());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((StorageActivity) Objects.requireNonNull(getActivity())).setBookFragment(this);
    }

    public void dataSetUpdate() {
        bookDetails.clear();
        bookDetails.addAll(dataBaseHelper.getAllBookDetails());
        bookViewAdapter.notifyDataSetChanged();
    }

    public void SearchTitle(View v1) {
        String q = Library_searchText.getText().toString();
        if (!q.equals("") && q.length() > 1) {
            bookDetails = dataBaseHelper.getSimilarBookDetails(q);
            bookListGenerate();
            Library_resetButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(), "Search query is not long enough", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadAllBooks() {
        bookDetails = dataBaseHelper.getAllBookDetails();
        bookListGenerate();
    }

    private void bookListGenerate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        Books_recyclerView.setLayoutManager(layoutManager);
        bookViewAdapter = new BookViewAdapter(getActivity(), bookDetails, dataBaseHelper);
        Books_recyclerView.setAdapter(bookViewAdapter);
    }

}
