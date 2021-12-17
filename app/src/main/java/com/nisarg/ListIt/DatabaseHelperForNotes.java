package com.nisarg.ListIt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelperForNotes extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "Listit.db";
    public final static String TABLE_NAME = "Notes";
    public final static String TABLE_NAME_LOCKED = "Notes_locked";
    public final static String TABLE_NAME_SHOPPING_LIST = "shopping_list_table";
    public final static String TABLE_NAME_TASK_LIST = "task_list_table";
    public final static String COL_Title = "TITLE";
    public final static String COL_CONTENT = "CONTENT";
    public final static String COL_PINNED = "PINNED";


    public String COL_DATE = "DATE";
    public String COL_NAME = "NAME";
    public String COL_QUANT = "QUANT";
    public String COL_SIZE = "SIZE";
    public String COL_PRICE = "PRICE";

    public String COL_DATE_TASK = "DATE";
    public String COL_TASK_STRING = "TASK";
    public String COL_PRIORITY = "PRIORITY";


    public DatabaseHelperForNotes(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                " TITLE TEXT," +
                " CONTENT TEXT," +
                " PINNED TEXT," +
                " DATE TEXT)");

        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_LOCKED + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                " TITLE TEXT," +
                " CONTENT TEXT," +
                " LOCKEDORNOT NUMBER(1)," +
                " DATE TEXT)");


        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_SHOPPING_LIST + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                " NAME TEXT," +
                " QUANT TEXT," +
                " SIZE TEXT," +
                " PRICE TEXT)");

        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_TASK_LIST + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                " DATE TEXT," +
                " TASK TEXT," +
                " PRIORITY TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCKED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TASK_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SHOPPING_LIST);
        onCreate(sqLiteDatabase);
    }

    public int updatePinnedStatus(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        return (db.update(TABLE_NAME, cv, "_id=?", new String[]{String.valueOf(id)}));
    }

    public Cursor viewDefault() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    public Cursor viewDefault_shoppingItem() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_SHOPPING_LIST;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    public Cursor viewDefault_for_locked() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LOCKED;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor viewDefault_for_task_list() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_TASK_LIST;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public int insertData(String title, String content, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_Title, title);
        contentValues.put(COL_CONTENT, content);
        contentValues.put(COL_DATE, date);
        int key = (int) db.insert(TABLE_NAME, null, contentValues);
        return key;
    }

    public int insertData_shoppingItem(String name, String quant, String size, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAME, name);
        contentValues.put(COL_QUANT, quant);
        contentValues.put(COL_SIZE, size);
        contentValues.put(COL_PRICE, price);

        int key = (int) db.insert(TABLE_NAME_SHOPPING_LIST, null, contentValues);
        return key;
    }

    public int insert_task(String task, String prio, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_TASK_STRING, task);
        cv.put(COL_DATE_TASK, date);
        cv.put(COL_PRIORITY, prio);

        int key = (int) db.insert(TABLE_NAME_TASK_LIST, null, cv);
        return key;
    }


    public long insertData_for_locked(String title, String content, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_Title, title);
        contentValues.put(COL_CONTENT, content);
        contentValues.put(COL_DATE, date);
        long key = (db.insert(TABLE_NAME_LOCKED, null, contentValues));
        return key;
    }

    public int update(int id, String t, String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_Title, t);
        cv.put(COL_CONTENT, n);
        return (db.update(TABLE_NAME, cv, "_id=?", new String[]{String.valueOf(id)}));

    }

    public int update_for_locked(int id, String t, String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_Title, t);
        cv.put(COL_CONTENT, n);
        return (db.update(TABLE_NAME_LOCKED, cv, "_id=?", new String[]{String.valueOf(id)}));

    }

    public int update_item(int id, String name, String price, String quantity, String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, name);
        cv.put(COL_PRICE, price);
        cv.put(COL_SIZE, size);
        cv.put(COL_QUANT, quantity);
        return (db.update(TABLE_NAME_SHOPPING_LIST, cv, "_id=?", new String[]{String.valueOf(id)}));
    }

    public Integer deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    public Integer deleteAllData_for_locked() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_LOCKED, null, null);
    }

    public Integer delete_all_items() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_TASK_LIST, null, null);
    }

    public Integer delete_for_locked(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_LOCKED, "_id=?", new String[]{String.valueOf(id)});
    }

    public Integer delete_item(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_SHOPPING_LIST, "_id=?", new String[]{String.valueOf(id)});
    }

    public Integer delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

    public int delete_task(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_TASK_LIST, "_id=?", new String[]{String.valueOf(id)});
    }

    public int update_task(int id, String updatedTask, int checkedRadioButtonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String updatedPriority;

        updatedPriority = (checkedRadioButtonId == R.id.high_priority) ? "high" : "normal";

        cv.put(COL_TASK_STRING, updatedTask);
        cv.put(COL_PRIORITY, updatedPriority);
        return (db.update(TABLE_NAME_TASK_LIST, cv, "_id=?", new String[]{String.valueOf(id)}));
    }

    public Cursor getDatesOftasks() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT %s FROM %s", COL_DATE_TASK, TABLE_NAME_TASK_LIST);
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
