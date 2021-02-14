package com.example.chamikanandasiri.domesticlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DomesticLibrary.db";
    private static final String TABLE_BOOK = "book_table";
    private static final String TABLE_USER = "user_table";
    private static final String TABLE_AUTHOR = "author_table";
    private static final String TABLE_RECEIPT = "receipt_table";
    private static final String TABLE_CATEGORY = "category_table";

    private String TAG = "Test";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_BOOK + " (BookID INTEGER Primary key AutoIncrement, TimeStamp TEXT, Title TEXT, AuthorID INTEGER, ISBN TEXT, Price TEXT, CategoryID INTEGER, Availability INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_USER + " (UserID INTEGER Primary key AutoIncrement, TimeStamp TEXT, Name TEXT, Telephone TEXT, Status INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_RECEIPT + " (ReceiptID INTEGER , TimeStamp TEXT, BookID INTEGER, UserID INTEGER)");
        sqLiteDatabase.execSQL("create table " + TABLE_CATEGORY + " (CategoryID INTEGER Primary key AutoIncrement, TimeStamp TEXT, Name TEXT)");
        sqLiteDatabase.execSQL("create table " + TABLE_AUTHOR + " (AuthorID INTEGER Primary key Autoincrement, TimeStamp TEXT, Name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        sqLiteDatabase.execSQL("DROP tABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP tABLE IF EXISTS " + TABLE_AUTHOR);
        sqLiteDatabase.execSQL("DROP tABLE IF EXISTS " + TABLE_CATEGORY);
        sqLiteDatabase.execSQL("DROP tABLE IF EXISTS " + TABLE_RECEIPT);
        onCreate(sqLiteDatabase);
    }

    public boolean insertRowBook(String title, String author, String isbn, String price,int categoryID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("Title", title);
        //cv.put("AuthorID", id);
        cv.put("ISBN", isbn);
        cv.put("Price", price);
        cv.put("CategoryID", categoryID);
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

    public boolean insertRowAuthor(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("Name", name);
        long result = db.insert(TABLE_AUTHOR, null, cv);
        return result != -1;
    }

    public boolean insertRowCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TimeStamp", addTimeStamp());
        cv.put("Name", name);
        long result = db.insert(TABLE_CATEGORY, null, cv);
        return result != -1;
    }

    private String addTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public Cursor getAllComments() {
        SQLiteDatabase db = this.getReadableDatabase();
        //return db.rawQuery("select Comment from " + TABLE_COMMENT, null);
        return db.query(TABLE_COMMENT, new String[]{"Comment"}, null, null, null, null, "TimeStamp");
    }

    public Cursor getPhraseCommentBookIDByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        //return db.rawQuery("select * from " + TABLE_COMMENT + " where Title = \'"+ title +"\'",null);
        return db.query(TABLE_COMMENT, new String[]{"Phrase", "Comment","BookID"}, "Title = ?", new String[]{title}, null, null, null);
    }

    public Cursor getPhraseCommentTitlesByBookID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_COMMENT, new String[]{"Title","Phrase", "Comment"}, "BookID = ?", new String[]{id}, null, null, null);
    }

    public Cursor getAllDistinctTitles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_COMMENT, new String[]{"Title"}, null, null, null, null, "TimeStamp", null);
    }

    public Cursor getSimilarTitles(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_COMMENT, new String[]{"Title"}, "Title" + " LIKE ?", new String[]{"%" + title + "%"}, null, null, "TimeStamp", null);
    }

    public Cursor getAllDistinctWords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_WORD, new String[]{"Word"}, null, null, null, null, "TimeStamp", null);
    }

    public Cursor getSimilarWords(String word) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_WORD, new String[]{"Word"}, "Word" + " LIKE ?", new String[]{"%" + word + "%"}, null, null, "TimeStamp", null);
    }

    public Cursor getDefinitionPoSByWord(String word) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_WORD, new String[]{"PartOfSpeech", "Definition"}, "Word = ?", new String[]{word}, null, null, null);
    }

    public Cursor getWordsByPoS(String pos) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_WORD, new String[]{"Word"}, "PartOfSpeech = ?", new String[]{pos}, null, null, null, null);
    }

    public Cursor getWordsPoSDefinitions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_WORD, new String[]{"Word", "PartOfSpeech", "Definition"}, null, null, null, null, null, null);
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOK, new String[]{"BookID"}, null, null, null, null, "TimeStamp");
    }

    public Cursor getAllBookDetailsByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOK, new String[]{"Title", "Author", "ISBN", "CoverURL"}, "BookID = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor getSimilarBookIDs(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_BOOK, new String[]{"BookID"}, "Title" + " LIKE ?", new String[]{"%" + title + "%"}, null, null, "TimeStamp", null);
    }

    public Cursor getRecentBookIDs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, TABLE_BOOK, new String[]{"BookID"}, null, null, null, null, "TimeStamp DESC", "3");
    }

    public Cursor getBookIDByISBN(String ISBN) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOK, new String[]{"BookID"}, "ISBN = ?", new String[]{ISBN}, null, null, null);
    }

    public Cursor getAllBookIDsTitles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOK, new String[]{"BookID", "Title"}, null, null, null, null, "TimeStamp");
    }

    public Cursor getContentDetailsByBooKID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CONTENT, new String[]{"ContentID", "Name", "ImageURL", "FileURL", "Animated"}, "BookID = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor getContentIDsByBooKID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CONTENT, new String[]{"ContentID"}, "BookID = ?", new String[]{id}, null, null, null, null);
    }

    public boolean deleteRowBook(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return ((db.delete(TABLE_BOOK, "BookID = ?", new String[]{id}) > 0) && (db.delete(TABLE_CONTENT, "BookID = ?", new String[]{id}) > 0));
    }

    public boolean deleteRowContent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TABLE_CONTENT, "ContentID = ?", new String[]{id}) > 0);
    }

}