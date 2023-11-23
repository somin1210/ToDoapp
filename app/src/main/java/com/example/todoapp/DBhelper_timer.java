package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper_timer extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final String TABLE_NAME = "YourTableName";
    public static final String COLUMN_RECORD = "record";
    public static final String COLUMN_MEMO = "memo";

    public DBhelper_timer(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORD + " TEXT, " + COLUMN_MEMO + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String record, String memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RECORD, record);
        contentValues.put(COLUMN_MEMO, memo);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // 데이터베이스로부터 모든 레코드를 검색하는 메서드
    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }
}