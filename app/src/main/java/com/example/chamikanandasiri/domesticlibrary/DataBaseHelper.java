package com.example.chamikanandasiri.domesticlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DomesticLibrary.db";
    private static final String TABLE_BOOK = "book_table";
    private static final String TABLE_USER = "user_table";
    private static final String TABLE_RECEIPT = "receipt_table";

    private String TAG = "Test";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_BOOK + " (BookID INTEGER Primary key AutoIncrement, TimeStamp TEXT, Code TEXT UNIQUE, Title TEXT UNIQUE, Author TEXT, ISBN TEXT UNIQUE, Price TEXT, Category TEXT, Availability INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_USER + " (UserID INTEGER Primary key AutoIncrement , TimeStamp TEXT, Name TEXT UNIQUE, Telephone TEXT, Status INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_RECEIPT + " (ReceiptID INTEGER Primary key AutoIncrement, TimeStamp TEXT, BookID INTEGER, UserID INTEGER, Completed INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIPT);
        onCreate(sqLiteDatabase);
    }

    //====================  BOOK TABLE  ==============

    public boolean insertRowBook(String bookCode, String title, String author, String isbn, String price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("Code", bookCode);
        cv.put("Title", title);
        cv.put("Author", author);
        cv.put("ISBN", isbn);
        cv.put("Price", price);
        cv.put("Category", category);
        cv.put("Availability", 1);
        long result = db.insert(TABLE_BOOK, null, cv);
        return result != -1;
    }

    public boolean updateBookAvailability(int id, boolean available) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Availability", available ? 1 : 0);
        long result = db.update(TABLE_BOOK, cv, "BookID = ?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    public boolean updateBookEntry(int id, ArrayList<String[]> keys) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < keys.size(); i++) {
            cv.put(keys.get(i)[0], keys.get(i)[1]);
            Log.d(TAG, "updateBookEntry: "+keys.get(i)[0]);
        }
        long result = db.update(TABLE_BOOK, cv, "BookID = ?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    public boolean deleteBookEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_BOOK,"BookID = ?",new String[]{String.valueOf(id)});
        boolean res2 = deleteEntryByBookID(id);
        return (result != -1) && res2;
    }

    public ArrayList<String> getAllAuthors() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"Author"}, null, null, "Author", null, "TimeStamp", null);
        ArrayList<String> authors = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            authors.add(a);
        }
        res.close();
        return authors;
    }

    public ArrayList<String> getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"Title"}, null, null, "Title", null, "TimeStamp", null);
        ArrayList<String> books = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            books.add(a);
        }
        res.close();
        return books;
    }

    public ArrayList<String[]> getAllBookDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"BookID", "Code", "Title", "Author", "Availability"}, null, null, null, null, "TimeStamp DESC", null);
        ArrayList<String[]> booksAuthors = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            String b = res.getString(1);
            String c = res.getString(2);
            String d = res.getString(3);
            String e = res.getString(4);
            booksAuthors.add(new String[]{a, b, c, d, e});
        }
        res.close();
        return booksAuthors;
    }

    public int getBookIDByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"BookID"}, "Title = ?", new String[]{title}, null, null, null, null);
        int id = 0;
        while (res.moveToNext()) {
            id = res.getInt(0);
        }
        res.close();
        return id;
    }

    public int getBookIDByCode(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"BookID"}, "Code = ?", new String[]{code}, null, null, null, null);
        int id = 0;
        while (res.moveToNext()) {
            id = res.getInt(0);
        }
        res.close();
        return id;
    }

    public String getBookTitleByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"Title"}, "BookID = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        String book = "Error";
        while (res.moveToNext()) {
            book = res.getString(0);
        }
        res.close();
        return book;
    }

    public String[] getBookDetailsByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resTitle = db.query(true, TABLE_BOOK, new String[]{"Code", "Title", "Author", "ISBN", "Price", "Category", "Availability"}, "BookID = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        String[] details = new String[]{};
        while (resTitle.moveToNext()) {
            String a = resTitle.getString(0);
            String b = resTitle.getString(1);
            String c = resTitle.getString(2);
            String d = resTitle.getString(3);
            String e = resTitle.getString(4);
            String f = resTitle.getString(5);
            String g = resTitle.getString(6);
            details = new String[]{a, b, c, d, e, f, g};
        }
        resTitle.close();
        return details;
    }

    public int getBookAvailabilityByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"Availability"}, "BookID = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        int av = 0;
        while (res.moveToNext()) {
            av = res.getInt(0);
        }
        res.close();
        return av;
    }

    public ArrayList<String[]> getSimilarBookDetails(String searchInput) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resTitle = db.query(true, TABLE_BOOK, new String[]{"BookID", "Code", "Title", "Author", "Availability"}, "Title LIKE ? OR Author LIKE ? OR Code LIKE ?", new String[]{"%" + searchInput + "%", "%" + searchInput + "%", "%" + searchInput + "%"}, null, null, "TimeStamp DESC", null);
        ArrayList<String[]> details = new ArrayList<>();
        while (resTitle.moveToNext()) {
            String a = resTitle.getString(0);
            String b = resTitle.getString(1);
            String c = resTitle.getString(2);
            String d = resTitle.getString(3);
            String e = resTitle.getString(4);
            details.add(new String[]{a, b, c, d, e});
        }
        resTitle.close();
        return details;
    }

    //=========================  TABLE USER   ==========================

    public boolean insertRowUser(String name, String telephone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("Name", name);
        cv.put("Telephone", telephone);
        cv.put("Status", 1);
        long result = db.insert(TABLE_USER, null, cv);
        return result != -1;
    }

    public boolean updateUserEntry(int id, ArrayList<String[]> keys) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < keys.size(); i++) {
            cv.put(keys.get(i)[0], keys.get(i)[1]);
        }
        long result = db.update(TABLE_USER, cv, "UserID = ?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    public boolean deleteUserEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_USER,"UserID = ?",new String[]{String.valueOf(id)});
        boolean res2 = deleteEntryByUserID(id);
        return (result != -1) && res2;
    }

    public ArrayList<String> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_USER, new String[]{"Name"}, null, null, "Name", null, "TimeStamp", null);
        ArrayList<String> users = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            users.add(a);
        }
        res.close();
        return users;
    }

    public ArrayList<String[]> getAllUserDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_USER, new String[]{"UserID", "Name", "Telephone"}, null, null, null, null, "TimeStamp DESC", "30");
        ArrayList<String[]> users = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            String b = res.getString(1);
            String c = res.getString(2);
            users.add(new String[]{a, b, c});
        }
        res.close();
        return users;
    }

    public String[] getUserDetailsByID(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_USER, new String[]{"Name", "Telephone"}, " UserID = ?", new String[]{String.valueOf(userID)}, null, null, null, null);
        String[] users = new String[]{};
        while (res.moveToNext()) {
            String a = res.getString(0);
            String b = res.getString(1);
            users = new String[]{a, b};
        }
        res.close();
        return users;
    }

    public int getUserIDByName(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_USER, new String[]{"UserID"}, "Name = ?", new String[]{user}, null, null, null, null);
        int id = 0;
        while (res.moveToNext()) {
            id = res.getInt(0);
        }
        res.close();
        return id;
    }

    public ArrayList<String[]> getSimilarUserDetails(String searchInput) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_USER, new String[]{"UserID", "Name", "Telephone"}, "Name" + " LIKE ?", new String[]{"%" + searchInput + "%"}, null, null, "TimeStamp DESC", null);
        ArrayList<String[]> users = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            String b = res.getString(1);
            String c = res.getString(2);
            users.add(new String[]{a, b, c});
        }
        res.close();
        return users;
    }

    //=========================  TABLE RECEIPT ==============================

    public boolean insertRowReceipt(int bookID, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("BookID", bookID);
        cv.put("UserID", userID);
        cv.put("Completed", 0);
        long result = db.insert(TABLE_RECEIPT, null, cv);
        return result != -1;
    }

    public boolean updateReceiptCompletion(String ReceiptID, String BookID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Completed", 1);
        long result = db.update(TABLE_RECEIPT, cv, "ReceiptID = ?", new String[]{ReceiptID});
        boolean success = updateBookAvailability(Integer.parseInt(BookID), true);
        return (result != -1) && success;
    }

    public boolean deleteEntryByBookID(int bookID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_RECEIPT,"BookID = ?",new String[]{String.valueOf(bookID)});
        boolean suc = updateBookAvailability(bookID,true);
        return (result != -1) && suc;
    }

    public boolean deleteEntryByUserID(int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String[]> s = getBorrowedBooksByUserID(userID);
        boolean suc = true;
        for (String[] m : s) {
            suc = suc && updateBookAvailability(Integer.parseInt(m[0]),true);
        }
        long result = db.delete(TABLE_RECEIPT,"UserID = ?",new String[]{String.valueOf(userID)});
        return (result != -1) && suc;
    }

    public ArrayList<String[]> getBorrowedBooksByUserID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_RECEIPT, new String[]{"BookID", "TimeStamp", "Completed","ReceiptID"}, "UserID = ?", new String[]{String.valueOf(id)}, null, null, "TimeStamp", null);
        ArrayList<String[]> borrowedBooks = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            String b = res.getString(1);
            String c = res.getString(2);
            String d = res.getString(3);
            borrowedBooks.add(new String[]{a, b, c, d});
        }
        res.close();
        return borrowedBooks;
    }

    public String getBorrowedUserByBookID(int bookID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resReceipt = db.query(true, TABLE_RECEIPT, new String[]{"UserID"}, "BookID = ?", new String[]{String.valueOf(bookID)}, null, null, "TimeStamp DESC", "1");
        int userID = 0;
        while (resReceipt.moveToNext()) {
            userID = resReceipt.getInt(0);
        }
        resReceipt.close();
        Cursor resUser = db.query(true, TABLE_USER, new String[]{"Name"}, "UserID = ?", new String[]{String.valueOf(userID)}, null, null, "TimeStamp DESC", "1");
        String userName = "Not Available";
        while (resUser.moveToNext()) {
            userName = resUser.getString(0);
        }
        resUser.close();
        return userName;
    }

    //============================ OTHER ===================

    private String addTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

}