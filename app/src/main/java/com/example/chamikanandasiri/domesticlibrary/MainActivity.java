package com.example.chamikanandasiri.domesticlibrary;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private Button main_addBookButton, main_addReceiptButton, main_addUserButton, main_directLibraryButton, addBook_closeButton, addBook_okButton, addUser_closeButton, addUser_okButton, addReceipt_closeButton, addReceipt_addButton, addReceipt_okButton;
    private Dialog main_addBookPopup, main_addUserPopup, main_addReceiptPopup;
    private EditText addBook_titleText, addBook_isbnText, addBook_priceText, addbook_idText, addUser_nameText, addUser_phoneText;
    private AutoCompleteTextView addBook_authorView, addReceipt_userView, addReceipt_bookView, getAddReceipt_bookDuplicateView;
    private LinearLayout addReceipt_addBookLayout, addReceipt_addBookItem;
    private Spinner addBook_categorySpinner;
    private ArrayList<String> authorsList, userList, bookList;
    private String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DataBaseHelper(this);
        authorsList = dbHelper.getAllAuthors();
        userList = dbHelper.getAllUsers();
        bookList = dbHelper.getAllBooks();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_addBookPopup = new Dialog(this);
        main_addBookPopup.setContentView(R.layout.popup_addbook);
        main_addUserPopup = new Dialog(this);
        main_addUserPopup.setContentView(R.layout.popup_adduser);
        main_addReceiptPopup = new Dialog(this);
        main_addReceiptPopup.setContentView(R.layout.popup_addreceipt);
        setupAddBookPopup();
        setupAddUserPopup();
        setupAddReceiptPopup();
        setupMainLayout();
    }

    private void setupMainLayout() {
        main_addBookButton = findViewById(R.id.btnMainAddBook);
        main_addBookButton.setOnClickListener(this::showAddBookPopup);
        main_addUserButton = findViewById(R.id.btnMainAddUser);
        main_addUserButton.setOnClickListener(this::showAddUserPopup);
        main_addReceiptButton = findViewById(R.id.btnMainAddReceipt);
        main_addReceiptButton.setOnClickListener(this::showAddReceiptPopup);
        main_directLibraryButton = findViewById(R.id.btnMainDirectLibrary);
        main_directLibraryButton.setOnClickListener(view -> loadLibrary());
    }

    private void setupAddBookPopup() {
        addBook_authorView = main_addBookPopup.findViewById(R.id.actAddBookAuthor);
        addBook_categorySpinner = main_addBookPopup.findViewById(R.id.spnAddBookCategory);
        addBook_closeButton = main_addBookPopup.findViewById(R.id.btnAddBookClose);
        addBook_isbnText = main_addBookPopup.findViewById(R.id.edtAddBookISBN);
        addBook_okButton = main_addBookPopup.findViewById(R.id.btnAddBookOK);
        addBook_priceText = main_addBookPopup.findViewById(R.id.edtAddBookPrice);
        addBook_titleText = main_addBookPopup.findViewById(R.id.edtAddBookTitle);
        addbook_idText = main_addBookPopup.findViewById(R.id.edtAddBookID);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.category_list));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBook_categorySpinner.setAdapter(categoryAdapter);
        setAuthorAdapter();
    }

    private void setupAddUserPopup() {
        addUser_closeButton = main_addUserPopup.findViewById(R.id.btnAddUserClose);
        addUser_okButton = main_addUserPopup.findViewById(R.id.btnAddUserOK);
        addUser_nameText = main_addUserPopup.findViewById(R.id.edtAddUserName);
        addUser_phoneText = main_addUserPopup.findViewById(R.id.edtAddUserTelephone);
    }

    private void setupAddReceiptPopup() {
        addReceipt_userView = main_addReceiptPopup.findViewById(R.id.actAddReceiptUser);
        addReceipt_bookView = main_addReceiptPopup.findViewById(R.id.actAddReceiptBook);
        addReceipt_addButton = main_addReceiptPopup.findViewById(R.id.btnAddReceiptAdd);
        addReceipt_closeButton = main_addReceiptPopup.findViewById(R.id.btnAddReceiptClose);
        addReceipt_okButton = main_addReceiptPopup.findViewById(R.id.btnAddReceiptOK);
        addReceipt_addBookLayout = main_addReceiptPopup.findViewById(R.id.lloAddReceiptAddBook);
        setUserAdapter();
        setBookAdapter();
    }

    private void setAuthorAdapter() {
        ArrayAdapter<String> authorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, authorsList);
        addBook_authorView.setAdapter(authorAdapter);
    }

    private void setBookAdapter() {
        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bookList);
        addReceipt_bookView.setAdapter(bookAdapter);
    }

    private void setUserAdapter() {
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, userList);
        addReceipt_userView.setAdapter(userAdapter);
    }

    public void showAddBookPopup(View v1) {
        addBook_closeButton.setOnClickListener(v2 -> main_addBookPopup.dismiss());
        addBook_okButton.setOnClickListener(v2 -> addBookToDatabase());
        main_addBookPopup.show();
    }

    public void showAddUserPopup(View v1) {
        addUser_closeButton.setOnClickListener(v2 -> main_addUserPopup.dismiss());
        addUser_okButton.setOnClickListener(v2 -> addUserToDatabase());
        main_addUserPopup.show();
    }

    public void showAddReceiptPopup(View v1) {
        addReceipt_closeButton.setOnClickListener(v2 -> main_addReceiptPopup.dismiss());
        addReceipt_addButton.setOnClickListener(v2 -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.listitem_addreceipt, null);
            AutoCompleteTextView tv = rowView.findViewById(R.id.actAddReceiptBookDuplicate);
            ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, bookList);
            tv.setAdapter(bookAdapter);
            addReceipt_addBookLayout.addView(rowView, addReceipt_addBookLayout.getChildCount() - 1);
        });
        addReceipt_okButton.setOnClickListener(view -> addReceiptToDatabase());
        main_addReceiptPopup.show();
    }

    private void addBookToDatabase() {
        String id = addbook_idText.getText().toString();
        String name = addBook_titleText.getText().toString().trim();
        String isbn = addBook_isbnText.getText().toString().trim();
        String price = addBook_priceText.getText().toString().trim();
        String author = addBook_authorView.getText().toString().trim();
        String category = addBook_categorySpinner.getSelectedItem().toString();
        if (!name.equals("") && !author.equals("") && !id.equals("") && !isbn.equals("")) {
            if (dbHelper.getBookIDByTitle(name) <= 0) {
                boolean success = dbHelper.insertRowBook(Integer.parseInt(id), name, author, isbn, price, category);
                if (success) {
                    if (!authorsList.contains(author)) {
                        authorsList.add(author);
                        setAuthorAdapter();
                    }
                    if (!bookList.contains(name)) {
                        bookList.add(name);
                        setBookAdapter();
                    }
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();

                    addbook_idText.setText("");
                    addBook_authorView.setText("");
                    addBook_isbnText.setText("");
                    addBook_titleText.setText("");
                    addBook_priceText.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error - Book is already in the database", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error - All fields should be filled", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLibrary() {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    private void addUserToDatabase() {
        String name = addUser_nameText.getText().toString().trim();
        String phone = addUser_phoneText.getText().toString().trim();
        if (!name.equals("")) {
            if (dbHelper.getUserIDByName(name) <= 0) {
                boolean success = dbHelper.insertRowUser(name, phone);
                if (success) {
                    if (!userList.contains(name)) {
                        userList.add(name);
                        setUserAdapter();
                    }
                    addUser_phoneText.setText("");
                    addUser_nameText.setText("");
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error - User is already in the database", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error - All fields should be filled", Toast.LENGTH_SHORT).show();
        }
    }

    private void addReceiptToDatabase() {
        String user = addReceipt_userView.getText().toString().trim();
        ArrayList<String> books = new ArrayList<>();
        books.add(addReceipt_bookView.getText().toString().trim());
        int childCount = addReceipt_addBookLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = addReceipt_addBookLayout.getChildAt(i);
            AutoCompleteTextView tv = v.findViewById(R.id.actAddReceiptBookDuplicate);
            books.add(tv.getText().toString().trim());
        }
        int userID = dbHelper.getUserIDByName(user);
        if (userID > 0) {
            for (String title : books) {
                int ID = dbHelper.getBookIDByTitle(title);
                if (ID <= 0) {
                    Toast.makeText(getApplicationContext(), "Error - " + title + " is not in the database", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "addReceiptToDatabase: " + ID + " " + userID);
                    boolean success = dbHelper.insertRowReceipt(ID, userID, 4345);
                    if (success) {
                        addReceipt_userView.setText("");
                        addReceipt_bookView.setText("");
                        for (int i = 0; i < addReceipt_addBookLayout.getChildCount(); i++) {
                            View v = addReceipt_addBookLayout.getChildAt(i);
                            AutoCompleteTextView tv = v.findViewById(R.id.actAddReceiptBookDuplicate);
                            onBookRemove(tv);
                        }
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error - User is not in the database", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBookRemove(View v) {
        addReceipt_addBookLayout.removeView((View) v.getParent());
    }

    //TODO - edit book details ability
    //TODO - disable buttons when necessary data not available
}
