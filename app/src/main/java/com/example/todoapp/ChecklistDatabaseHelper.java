package com.example.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChecklistDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "checklist.db";
    private static final int DATABASE_VERSION = 1;

    public ChecklistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE checklist (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "text TEXT, " +
                "isChecked INTEGER, " +
                "createdTime INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 스키마를 업그레이드하는 코드를 작성합니다.
    }
}