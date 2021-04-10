package com.example.chamikanandasiri.domesticlibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class StorageActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private TextView viewBook_titleView, viewBook_authorView, viewBook_codeView, viewBook_isbnView, viewBook_categoryView, viewBook_priceView, viewBook_availabilityView, viewUser_nameView, viewUser_telephoneView, viewUser_countView;
    private ConstraintLayout bookDetails_ViewLayout, bookDetails_EditLayout, userDetails_ViewLayout, userDetails_EditLayout;
    private FloatingActionButton addBook_fab, addUser_fab, addReceipt_fab;
    private Button addBook_okButton, addUser_okButton, addReceipt_closeButton, addReceipt_addButton, addReceipt_okButton;
    private ImageButton addBook_closeButton, addUser_closeButton, viewBook_editButton, viewBook_deleteButton, viewUser_editButton, viewUser_deleteButton;
    private Dialog main_bookDetailsPopup, main_userDetailsPopup, main_addReceiptPopup;
    private EditText addBook_titleText, addBook_isbnText, addBook_priceText, addBook_codeText, addUser_nameText, addUser_phoneText;
    private AutoCompleteTextView addBook_authorView, addReceipt_userView, addReceipt_bookView;
    private LinearLayout addReceipt_addBookLayout;
    private Spinner addBook_categorySpinner;
    private RecyclerView viewUser_bookList;
    private ArrayList<String> authorsList, userList, bookList;
    private ArrayAdapter<String> categoryAdapter;
    private int currentTab = 0;
    private boolean fabVisible = true;
    private String TAG = "test";
    private UsersFragment usersFragment;
    private BooksFragment booksFragment;
    private AcceptBookViewAdapter acceptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new DataBaseHelper(this);
        authorsList = dbHelper.getAllAuthors();
        userList = dbHelper.getAllUsers();
        bookList = dbHelper.getAllBooks();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        main_bookDetailsPopup = new Dialog(this);
        main_bookDetailsPopup.setContentView(R.layout.popup_bookdetails);
        Objects.requireNonNull(main_bookDetailsPopup.getWindow()).setBackgroundDrawableResource(R.color.colorTransparent);
        main_userDetailsPopup = new Dialog(this);
        main_userDetailsPopup.setContentView(R.layout.popup_userdetails);
        Objects.requireNonNull(main_userDetailsPopup.getWindow()).setBackgroundDrawableResource(R.color.colorTransparent);
        main_addReceiptPopup = new Dialog(this);
        main_addReceiptPopup.setContentView(R.layout.popup_addreceipt);

        addBook_fab = findViewById(R.id.fabAddBook);
        addUser_fab = findViewById(R.id.fabAddUser);
        addReceipt_fab = findViewById(R.id.fabAddReceipt);
        addBook_fab.setOnClickListener(view -> showAddBookPopup());
        addUser_fab.setOnClickListener(view -> showAddUserPopup());
        addReceipt_fab.setOnClickListener(view -> showAddReceiptPopup());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupAddBookPopup();
        setupAddUserPopup();
        setupAddReceiptPopup();
    }

    public void setBookFragment(BooksFragment bf) {
        booksFragment = bf;
    }

    public void setUserFragment(UsersFragment uf) {
        usersFragment = uf;
    }

    private void setupAddBookPopup() {
        bookDetails_EditLayout = main_bookDetailsPopup.findViewById(R.id.cloBookDetailsEdit);
        bookDetails_ViewLayout = main_bookDetailsPopup.findViewById(R.id.cloBookDetailsView);
        addBook_authorView = main_bookDetailsPopup.findViewById(R.id.actBookDetailsAuthor);
        addBook_categorySpinner = main_bookDetailsPopup.findViewById(R.id.spnBookDetailsCategory);
        addBook_closeButton = main_bookDetailsPopup.findViewById(R.id.btnBookDetailsClose);
        addBook_isbnText = main_bookDetailsPopup.findViewById(R.id.edtBookDetailsISBN);
        addBook_okButton = main_bookDetailsPopup.findViewById(R.id.btnBookDetailsOK);
        addBook_priceText = main_bookDetailsPopup.findViewById(R.id.edtBookDetailsPrice);
        addBook_titleText = main_bookDetailsPopup.findViewById(R.id.edtBookDetailsTitle);
        addBook_codeText = main_bookDetailsPopup.findViewById(R.id.edtBookDetailsCode);
        viewBook_authorView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsAuthor);
        viewBook_categoryView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsCategory);
        viewBook_codeView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsCode);
        viewBook_isbnView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsISBN);
        viewBook_priceView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsPrice);
        viewBook_titleView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsTitle);
        viewBook_availabilityView = main_bookDetailsPopup.findViewById(R.id.txvBookDetailsAvailability);
        viewBook_editButton = main_bookDetailsPopup.findViewById(R.id.btnBookDetailsEdit);
        viewBook_deleteButton = main_bookDetailsPopup.findViewById(R.id.btnBookDetailsDelete);
        categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.category_list));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBook_categorySpinner.setAdapter(categoryAdapter);
        setAuthorAdapter();
    }

    private void setupAddUserPopup() {
        userDetails_EditLayout = main_userDetailsPopup.findViewById(R.id.cloUserDetailsEdit);
        userDetails_ViewLayout = main_userDetailsPopup.findViewById(R.id.cloUserDetailsView);
        viewUser_nameView = main_userDetailsPopup.findViewById(R.id.txvUserDetailsName);
        viewUser_telephoneView = main_userDetailsPopup.findViewById(R.id.txvUserDetailsTelephone);
        viewUser_countView = main_userDetailsPopup.findViewById(R.id.txvUserDetailsCount);
        addUser_closeButton = main_userDetailsPopup.findViewById(R.id.btnUserDetailsClose);
        addUser_okButton = main_userDetailsPopup.findViewById(R.id.btnUserDetailsOK);
        addUser_nameText = main_userDetailsPopup.findViewById(R.id.edtUserDetailsName);
        addUser_phoneText = main_userDetailsPopup.findViewById(R.id.edtUserDetailsTelephone);
        viewUser_editButton = main_userDetailsPopup.findViewById(R.id.btnUserDetailsEdit);
        viewUser_deleteButton = main_userDetailsPopup.findViewById(R.id.btnUserDetailsDelete);
        viewUser_bookList = main_userDetailsPopup.findViewById(R.id.lstUserDetailsComplete);
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

    public void showAddBookPopup() {
        addBook_closeButton.setOnClickListener(v2 -> {
            addBook_codeText.setText("");
            addBook_authorView.setText("");
            addBook_isbnText.setText("");
            addBook_titleText.setText("");
            addBook_priceText.setText("");
            main_bookDetailsPopup.dismiss();
        });
        addBook_okButton.setOnClickListener(v2 -> addBookToDatabase());
        bookDetails_ViewLayout.setVisibility(View.GONE);
        bookDetails_EditLayout.setVisibility(View.VISIBLE);
        main_bookDetailsPopup.show();
    }

    public void showViewBookPopup(int bookID) {
        addBook_closeButton.setOnClickListener(v2 -> main_bookDetailsPopup.dismiss());
        String[] bookDetails = dbHelper.getBookDetailsByID(bookID);
        viewBook_editButton.setOnClickListener(v2 -> showEditBookPopup(bookID, bookDetails));
        viewBook_deleteButton.setOnClickListener(v2 -> {
            dbHelper.deleteBookEntry(bookID);
            main_bookDetailsPopup.dismiss();
            updateTabs();
        });
        viewBook_codeView.setText(bookDetails[0]);
        viewBook_titleView.setText(bookDetails[1]);
        viewBook_authorView.setText(bookDetails[2]);
        viewBook_isbnView.setText(bookDetails[3]);
        String price = "Rs." + bookDetails[4] + ".00";
        viewBook_priceView.setText(bookDetails[4].equals("") ? "" : price);
        viewBook_categoryView.setText(bookDetails[5]);
        String availability = bookDetails[6];
        viewBook_availabilityView.setText(availability.equals("1") ? "Available" : "borrowed by " + dbHelper.getBorrowedUserByBookID(bookID));
        viewBook_availabilityView.setTextColor(availability.equals("1") ? getResources().getColor(R.color.colorAccentDark) : getResources().getColor(R.color.colorError));
        bookDetails_EditLayout.setVisibility(View.GONE);
        bookDetails_ViewLayout.setVisibility(View.VISIBLE);
        main_bookDetailsPopup.show();
    }

    public void showEditBookPopup(int id, String[] bookDetails) {
        addBook_closeButton.setOnClickListener(v2 -> {
            addBook_codeText.setText("");
            addBook_authorView.setText("");
            addBook_isbnText.setText("");
            addBook_titleText.setText("");
            addBook_priceText.setText("");
            main_bookDetailsPopup.dismiss();
        });
        addBook_okButton.setOnClickListener(v2 -> editBookEntry(id, bookDetails));
        addBook_codeText.setText(bookDetails[0]);
        addBook_titleText.setText(bookDetails[1]);
        addBook_authorView.setText(bookDetails[2]);
        addBook_isbnText.setText(bookDetails[3]);
        addBook_priceText.setText(bookDetails[4]);
        addBook_categorySpinner.setSelection(categoryAdapter.getPosition(bookDetails[5]));
        bookDetails_ViewLayout.setVisibility(View.GONE);
        bookDetails_EditLayout.setVisibility(View.VISIBLE);
        main_bookDetailsPopup.show();
    }

    public void showAddUserPopup() {
        addUser_closeButton.setOnClickListener(v2 -> main_userDetailsPopup.dismiss());
        addUser_okButton.setOnClickListener(v2 -> addUserToDatabase());
        userDetails_ViewLayout.setVisibility(View.GONE);
        userDetails_EditLayout.setVisibility(View.VISIBLE);
        main_userDetailsPopup.show();
    }

    public void showViewUserPopup(int userID) {
        addUser_closeButton.setOnClickListener(v2 -> main_userDetailsPopup.dismiss());
        String[] userDetails = dbHelper.getUserDetailsByID(userID);
        viewUser_editButton.setOnClickListener(view -> showEditUserPopup(userID, userDetails));
        viewUser_deleteButton.setOnClickListener(v2 -> {
            dbHelper.deleteUserEntry(userID);
            main_userDetailsPopup.dismiss();
            updateTabs();
        });
        viewUser_nameView.setText(userDetails[0]);
        viewUser_telephoneView.setText(userDetails[1]);
        ArrayList<String[]> borrows = dbHelper.getBorrowedBooksByUserID(userID);
        int bookCount = borrows.size();
        String countText = bookCount + " Books";
        viewUser_countView.setText(countText);

        ArrayList<String[]> borrowedBooks = new ArrayList<>();
        for (String[] s : borrows) {
            if (!s[2].equals("1")) {
                borrowedBooks.add(new String[]{s[0], dbHelper.getBookTitleByID(Integer.parseInt(s[0])), s[1].substring(0, 10), s[3]});
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewUser_bookList.setLayoutManager(layoutManager);
        acceptAdapter = new AcceptBookViewAdapter(this, borrowedBooks, dbHelper, String.valueOf(userID));
        viewUser_bookList.setAdapter(acceptAdapter);

        userDetails_EditLayout.setVisibility(View.GONE);
        userDetails_ViewLayout.setVisibility(View.VISIBLE);
        main_userDetailsPopup.show();
    }

    public void showEditUserPopup(int id, String[] userDetails) {
        addUser_closeButton.setOnClickListener(v2 -> {
            addUser_nameText.setText("");
            addUser_phoneText.setText("");
            main_userDetailsPopup.dismiss();
        });
        addUser_okButton.setOnClickListener(v2 -> editUserEntry(id, userDetails));
        addUser_nameText.setText(userDetails[0]);
        addUser_phoneText.setText(userDetails[1]);
        userDetails_ViewLayout.setVisibility(View.GONE);
        userDetails_EditLayout.setVisibility(View.VISIBLE);
        main_userDetailsPopup.show();
    }

    public void showAddReceiptPopup() {
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
        String code = addBook_codeText.getText().toString().trim();
        String name = addBook_titleText.getText().toString().trim();
        String isbn = addBook_isbnText.getText().toString().trim();
        String price = addBook_priceText.getText().toString().trim();
        String author = addBook_authorView.getText().toString().trim();
        String category = addBook_categorySpinner.getSelectedItem().toString();

        if (name.equals("") || author.equals("") || code.equals("") || isbn.equals("")) {
            Toast.makeText(getApplicationContext(), "Error - All fields should be filled", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.getBookIDByTitle(name) > 0) {
            Toast.makeText(getApplicationContext(), "Error - Book is already in the database", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.getBookIDByCode(code) > 0) {
            Toast.makeText(getApplicationContext(), "Error - Code is already used in the database", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = dbHelper.insertRowBook(code, name, author, isbn, price, category);
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

                booksFragment.dataSetUpdate();

                addBook_codeText.setText("");
                addBook_authorView.setText("");
                addBook_isbnText.setText("");
                addBook_titleText.setText("");
                addBook_priceText.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void editBookEntry(int bookID, String[] bookDetails) {
        String code = addBook_codeText.getText().toString().trim();
        String name = addBook_titleText.getText().toString().trim();
        String isbn = addBook_isbnText.getText().toString().trim();
        String price = addBook_priceText.getText().toString().trim();
        String author = addBook_authorView.getText().toString().trim();
        String category = addBook_categorySpinner.getSelectedItem().toString();

        if (name.equals("") || author.equals("") || code.equals("") || isbn.equals("")) {
            Toast.makeText(getApplicationContext(), "Error - All fields should be filled", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.getBookIDByTitle(name) != bookID && dbHelper.getBookIDByTitle(name) != 0) {
            Toast.makeText(getApplicationContext(), "Error - Book is already in the database", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.getBookIDByCode(code) != bookID && dbHelper.getBookIDByCode(code) != 0) {
            Toast.makeText(getApplicationContext(), "Error - Code is already used in the database", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String[]> keysValues = new ArrayList<>();
            if (!code.equals(bookDetails[0])) {
                keysValues.add(new String[]{"Code", code});
            }
            if (!name.equals(bookDetails[1])) {
                keysValues.add(new String[]{"Title", name});
            }
            if (!author.equals(bookDetails[2])) {
                keysValues.add(new String[]{"Author", author});
            }
            if (!isbn.equals(bookDetails[3])) {
                keysValues.add(new String[]{"ISBN", isbn});
            }
            if (!price.equals(bookDetails[4])) {
                keysValues.add(new String[]{"Price", price});
            }
            if (!category.equals(bookDetails[5])) {
                keysValues.add(new String[]{"Category", category});
            }
            if (keysValues.size() > 0) {
                boolean success = dbHelper.updateBookEntry(bookID, keysValues);
                if (success) {
                    if (!authorsList.contains(author)) {
                        authorsList.add(author);
                        setAuthorAdapter();
                    }
                    if (!bookList.contains(name)) {
                        bookList.add(name);
                        setBookAdapter();
                    }
                    Toast.makeText(getApplicationContext(), "Edited Successfully", Toast.LENGTH_SHORT).show();

                    booksFragment.dataSetUpdate();
                    usersFragment.dataSetUpdate();

                    addBook_codeText.setText("");
                    addBook_authorView.setText("");
                    addBook_isbnText.setText("");
                    addBook_titleText.setText("");
                    addBook_priceText.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "No changes made", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void editUserEntry(int userID, String[] userDetails) {
        String name = addUser_nameText.getText().toString().trim();
        String telephone = addUser_phoneText.getText().toString().trim();

        if (name.equals("") || telephone.equals("")) {
            Toast.makeText(getApplicationContext(), "Error - All fields should be filled", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.getBookIDByTitle(name) != userID && dbHelper.getBookIDByTitle(name) != 0) {
            Toast.makeText(getApplicationContext(), "Error - Name is already in the database", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String[]> keysValues = new ArrayList<>();
            if (!name.equals(userDetails[0])) {
                keysValues.add(new String[]{"Name", name});
            }
            if (!telephone.equals(userDetails[1])) {
                keysValues.add(new String[]{"Telephone", telephone});
            }
            if (keysValues.size() > 0) {
                boolean success = dbHelper.updateUserEntry(userID, keysValues);
                if (success) {
                    if (!userList.contains(name)) {
                        userList.add(name);
                        setUserAdapter();
                    }
                    Toast.makeText(getApplicationContext(), "Edited Successfully", Toast.LENGTH_SHORT).show();

                    booksFragment.dataSetUpdate();
                    usersFragment.dataSetUpdate();

                    addUser_nameText.setText("");
                    addUser_phoneText.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "No changes made", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addUserToDatabase() {
        String name = addUser_nameText.getText().toString().trim();
        String phone = addUser_phoneText.getText().toString().trim();
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Error - All fields should be filled", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.getUserIDByName(name) > 0) {
            Toast.makeText(getApplicationContext(), "Error - User is already in the database", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = dbHelper.insertRowUser(name, phone);
            if (success) {
                if (!userList.contains(name)) {
                    userList.add(name);
                    setUserAdapter();
                }

                usersFragment.dataSetUpdate();
                addUser_phoneText.setText("");
                addUser_nameText.setText("");
                Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error - Unsuccessful", Toast.LENGTH_SHORT).show();
            }
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
        if (userID <= 0) {
            Toast.makeText(getApplicationContext(), "Error - User is not in the database", Toast.LENGTH_SHORT).show();
        } else {
            for (String title : books) {
                int ID = dbHelper.getBookIDByTitle(title);
                if (ID <= 0) {
                    Toast.makeText(getApplicationContext(), "Error - " + title + " is not in the database", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.getBookAvailabilityByID(ID) <= 0) {
                    Toast.makeText(getApplicationContext(), "Error - " + title + " is not available", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "addReceiptToDatabase: " + ID + " " + userID);
                    boolean success = dbHelper.insertRowReceipt(ID, userID);
                    boolean success2 = dbHelper.updateBookAvailability(ID, false);
                    if (success && success2) {
                        booksFragment.dataSetUpdate();
                        usersFragment.dataSetUpdate();
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
        }
    }

    public void updateTabs() {
        usersFragment.dataSetUpdate();
        booksFragment.dataSetUpdate();
    }

    public void onBookRemove(View v) {
        addReceipt_addBookLayout.removeView((View) v.getParent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnu_Statistics) {
            showAddBookPopup();
        } else if (id == R.id.mnu_Settings) {
            showAddUserPopup();
        }
        return super.onOptionsItemSelected(item);
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                addUser_fab.hide();
                addBook_fab.show();
                addReceipt_fab.show();
                currentTab = 0;
                new Handler(Looper.getMainLooper()).postDelayed(() -> addBook_fab.show(), 100);
                break;
            case 1:
                addBook_fab.hide();
                addUser_fab.show();
                addReceipt_fab.show();
                currentTab = 1;
                new Handler(Looper.getMainLooper()).postDelayed(() -> addUser_fab.show(), 100);
                break;
        }
    }

    public void controlFABVisibility(float scroll) {
        if ((scroll < -5.0) && (!fabVisible)) {
            fabVisible = true;
            if (currentTab == 0) {
                addBook_fab.show();
                addReceipt_fab.show();
            } else {
                addUser_fab.show();
                addReceipt_fab.show();
            }
        } else if ((scroll > 5.0) && (fabVisible)) {
            fabVisible = false;
            addBook_fab.hide();
            addUser_fab.hide();
            addReceipt_fab.hide();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}