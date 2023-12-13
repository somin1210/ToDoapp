package com.example.todoapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper databaseHelper;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables SQL execution
        String CREATE_Note_TABLE = "CREATE TABLE " + Config.TABLE_NOTE + "("
                + Config.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_NOTE_TITLE + " TEXT NOT NULL, "
                + Config.COLUMN_NOTE_DATE + " TEXT NOT NULL, "
                + Config.COLUMN_NOTE_CONTENT + " TEXT NOT NULL "
                + ")";
        db.execSQL(CREATE_Note_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_NOTE);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}