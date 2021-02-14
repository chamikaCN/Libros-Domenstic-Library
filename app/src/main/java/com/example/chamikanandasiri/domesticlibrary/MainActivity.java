package com.example.chamikanandasiri.domesticlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Button main_addBookButton, main_addAuthorButton, main_addReceiptButton, main_addUserButton, addBook_closeButton, addBook_okButton, addUser_closeButton, addUser_okButton;
    private Dialog main_addBookPopup, main_addUserPopup;
    private EditText addBook_titleText, addBook_authorText, addBook_isbnText, addBook_priceText, addUser_nameText, addUser_phoneText;
    private Spinner addBook_categorySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_addBookPopup = new Dialog(this);
        main_addBookPopup.setContentView(R.layout.popup_addbook);
        main_addUserPopup = new Dialog(this);
        main_addUserPopup.setContentView(R.layout.popup_adduser);
        setupAddBookPopup();
        setupAddUserPopup();
        setupMainLayout();
    }

    private void setupMainLayout(){
        main_addBookButton = findViewById(R.id.btnAddBook);
        main_addBookButton.setOnClickListener(this::showAddBookPopup);
        main_addBookButton = findViewById(R.id.btnAddUser);
        main_addBookButton.setOnClickListener(this::showAddUserPopup);
    }

    private void setupAddBookPopup(){
        addBook_authorText = main_addBookPopup.findViewById(R.id.edtAddBookAuthor);
        addBook_categorySpinner = main_addBookPopup.findViewById(R.id.spnAddBookCategory);
        addBook_closeButton = main_addBookPopup.findViewById(R.id.btnAddBookClose);
        addBook_isbnText = main_addBookPopup.findViewById(R.id.edtAddBookISBN);
        addBook_okButton = main_addBookPopup.findViewById(R.id.btnAddBookOK);
        addBook_priceText = main_addBookPopup.findViewById(R.id.edtAddBookPrice);
        addBook_titleText = main_addBookPopup.findViewById(R.id.edtAddBookTitle);
    }

    private void setupAddUserPopup(){
        addUser_closeButton = main_addUserPopup.findViewById(R.id.btnAddUserClose);
        addUser_okButton = main_addUserPopup.findViewById(R.id.btnAddUserOK);
        addUser_nameText = main_addUserPopup.findViewById(R.id.edtAddUserName);
        addUser_phoneText = main_addUserPopup.findViewById(R.id.edtAddUserTelephone);
    }

    public void showAddBookPopup(View v1) {
        addBook_closeButton.setOnClickListener(v2 -> main_addBookPopup.dismiss());
        addBook_okButton.setOnClickListener(v2 -> main_addBookPopup.dismiss());
        main_addBookPopup.show();
    }

    public void showAddUserPopup(View v1) {
        addUser_closeButton.setOnClickListener(v2 -> main_addUserPopup.dismiss());
        addUser_okButton.setOnClickListener(v2 -> main_addUserPopup.dismiss());
        main_addUserPopup.show();
    }
}
