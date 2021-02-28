package com.example.chamikanandasiri.domesticlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        sqLiteDatabase.execSQL("create table " + TABLE_BOOK + " (BookID INTEGER Primary key , TimeStamp TEXT, Title TEXT UNIQUE, Author TEXT, ISBN TEXT UNIQUE, Price TEXT, Category TEXT, Availability INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_USER + " (UserID INTEGER Primary key AutoIncrement , TimeStamp TEXT, Name TEXT UNIQUE, Telephone TEXT, Status INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_RECEIPT + " (ReceiptID INTEGER, TimeStamp TEXT, BookID INTEGER, UserID INTEGER, Completed INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        sqLiteDatabase.execSQL("DROP tABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP tABLE IF EXISTS " + TABLE_RECEIPT);
        onCreate(sqLiteDatabase);
    }

    public boolean insertRowBook(int bookID, String title, String author, String isbn, String price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("BookID", bookID);
        cv.put("Title", title);
        cv.put("Author", author);
        cv.put("ISBN", isbn);
        cv.put("Price", price);
        cv.put("Category", category);
        cv.put("Availability", 1);
        long result = db.insert(TABLE_BOOK, null, cv);
        return result != -1;
    }

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

    public boolean insertRowReceipt(int bookID, int userID, int receiptID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ReceiptID", receiptID);
        cv.put("TimeStamp", addTimeStamp());
        cv.put("BookID", bookID);
        cv.put("UserID", userID);
        cv.put("Completed", 0);
        long result = db.insert(TABLE_RECEIPT, null, cv);
        return result != -1;
    }

    private String addTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
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

    public ArrayList<String[]> getAllBookDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"Title", "Author"}, null, null, null, null, "TimeStamp DESC", "20");
        ArrayList<String[]> booksAuthors = new ArrayList<>();
        while (res.moveToNext()) {
            String a = res.getString(0);
            String b = res.getString(1);
            booksAuthors.add(new String[]{a, b});
        }
        res.close();
        return booksAuthors;
    }

    public int getUserIDByName(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_USER, new String[]{"UserID"}, "Name = ?", new String[]{user}, null, null, null , null);
        int id = 0;
        while (res.moveToNext()) {
            id = res.getInt(0);
        }
        res.close();
        return id;
    }

    public int getBookIDByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true, TABLE_BOOK, new String[]{"BookID"}, "Title = ?", new String[]{title}, null, null, null , null);
        int id = 0;
        while (res.moveToNext()) {
            id = res.getInt(0);
        }
        res.close();
        return id;
    }

    public ArrayList<String[]> getSimilarBookDetails(String searchInput) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resTitle = db.query(true, TABLE_BOOK, new String[]{"Title", "Author"}, "Title" + " LIKE ?", new String[]{"%" + searchInput + "%"}, null, null, "TimeStamp", null);
        Cursor resAuthor = db.query(true, TABLE_BOOK, new String[]{"Title", "Author"}, "Author" + " LIKE ?", new String[]{"%" + searchInput + "%"}, null, null, "TimeStamp", null);
        ArrayList<String[]> details = new ArrayList<>();
        while (resTitle.moveToNext()) {
            String a = resTitle.getString(0);
            String b = resTitle.getString(1);
            details.add(new String[]{a, b});
        }
        resTitle.close();
        while (resAuthor.moveToNext()) {
            String a = resAuthor.getString(0);
            String b = resAuthor.getString(1);
            details.add(new String[]{a, b});
        }
        resAuthor.close();
        return details;
    }

    //===============================================================================================================================

//    public Cursor getPhraseCommentBookIDByTitle(String title) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        //return db.rawQuery("select * from " + TABLE_COMMENT + " where Title = \'"+ title +"\'",null);
//        return db.query(TABLE_COMMENT, new String[]{"Phrase", "Comment","BookID"}, "Title = ?", new String[]{title}, null, null, null);
//    }
//
//    public Cursor getPhraseCommentTitlesByBookID(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_COMMENT, new String[]{"Title","Phrase", "Comment"}, "BookID = ?", new String[]{id}, null, null, null);
//    }
//
//    public Cursor getAllDistinctTitles() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_COMMENT, new String[]{"Title"}, null, null, null, null, "TimeStamp", null);
//    }
//
//    public Cursor getSimilarTitles(String title) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_COMMENT, new String[]{"Title"}, "Title" + " LIKE ?", new String[]{"%" + title + "%"}, null, null, "TimeStamp", null);
//    }
//
//    public Cursor getAllDistinctWords() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_WORD, new String[]{"Word"}, null, null, null, null, "TimeStamp", null);
//    }
//
//    public Cursor getSimilarWords(String word) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_WORD, new String[]{"Word"}, "Word" + " LIKE ?", new String[]{"%" + word + "%"}, null, null, "TimeStamp", null);
//    }
//
//    public Cursor getDefinitionPoSByWord(String word) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_WORD, new String[]{"PartOfSpeech", "Definition"}, "Word = ?", new String[]{word}, null, null, null);
//    }
//
//    public Cursor getWordsByPoS(String pos) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_WORD, new String[]{"Word"}, "PartOfSpeech = ?", new String[]{pos}, null, null, null, null);
//    }
//
//    public Cursor getWordsPoSDefinitions() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_WORD, new String[]{"Word", "PartOfSpeech", "Definition"}, null, null, null, null, null, null);
//    }
//
//    public Cursor getAllBooks() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_BOOK, new String[]{"BookID"}, null, null, null, null, "TimeStamp");
//    }
//
//    public Cursor getAllBookDetailsByID(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_BOOK, new String[]{"Title", "Author", "ISBN", "CoverURL"}, "BookID = ?", new String[]{id}, null, null, null, null);
//    }
//
//    public Cursor getSimilarBookIDs(String title) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_BOOK, new String[]{"BookID"}, "Title" + " LIKE ?", new String[]{"%" + title + "%"}, null, null, "TimeStamp", null);
//    }
//
//    public Cursor getRecentBookIDs() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(true, TABLE_BOOK, new String[]{"BookID"}, null, null, null, null, "TimeStamp DESC", "3");
//    }
//
//    public Cursor getBookIDByISBN(String ISBN) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_BOOK, new String[]{"BookID"}, "ISBN = ?", new String[]{ISBN}, null, null, null);
//    }
//
//    public Cursor getAllBookIDsTitles() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_BOOK, new String[]{"BookID", "Title"}, null, null, null, null, "TimeStamp");
//    }
//
//    public Cursor getContentDetailsByBooKID(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_CONTENT, new String[]{"ContentID", "Name", "ImageURL", "FileURL", "Animated"}, "BookID = ?", new String[]{id}, null, null, null, null);
//    }
//
//    public Cursor getContentIDsByBooKID(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_CONTENT, new String[]{"ContentID"}, "BookID = ?", new String[]{id}, null, null, null, null);
//    }
//
//    public boolean deleteRowBook(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return ((db.delete(TABLE_BOOK, "BookID = ?", new String[]{id}) > 0) && (db.delete(TABLE_CONTENT, "BookID = ?", new String[]{id}) > 0));
//    }
//
//    public boolean deleteRowContent(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return (db.delete(TABLE_CONTENT, "ContentID = ?", new String[]{id}) > 0);
//    }

}