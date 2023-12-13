package com.example.todoapp.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.todoapp.model.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseManager {

    private final Context context;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public void insertNote(Note note) {
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_NOTE_TITLE, note.getTitle());
        contentValues.put(Config.COLUMN_NOTE_DATE, note.getDate());
        contentValues.put(Config.COLUMN_NOTE_CONTENT, note.getContent());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_NOTE, null, contentValues);
        } catch (SQLiteException e) {
            //Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
    }

    public List<Note> getAllNotes() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase(); Cursor cursor = sqLiteDatabase.query(Config.TABLE_NOTE, null, null, null, null, null, null, null)) {

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Note> NoteList = new ArrayList<>();
                    do {
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_NOTE_ID));
                        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE_TITLE));
                        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE_DATE));
                        @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE_CONTENT));
                        NoteList.add(new Note(id, title, date, content));
                    } while (cursor.moveToNext());

                    return NoteList;
                }
        } catch (Exception e) {
            //Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        }

        return Collections.emptyList();
    }


    public Note getNoteById(long idNote) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Note Note = null;
        try {
            cursor = sqLiteDatabase.query(Config.TABLE_NOTE, null,
                    Config.COLUMN_NOTE_ID + " = ? ", new String[]{String.valueOf(idNote)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_NOTE_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE_TITLE));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE_DATE));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE_CONTENT));
                Note = new Note(id, title, date, content);
            }
        } catch (Exception e) {
            //Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Note;
    }

    public void updateNote(Note Note) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_NOTE_TITLE, Note.getTitle());
        contentValues.put(Config.COLUMN_NOTE_DATE, Note.getDate());
        contentValues.put(Config.COLUMN_NOTE_CONTENT, Note.getContent());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_NOTE, contentValues,
                    Config.COLUMN_NOTE_ID + " = ? ",
                    new String[]{String.valueOf(Note.getId())});
        } catch (SQLiteException e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

    }

    public void deleteNoteById(long idNote) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_NOTE,
                    Config.COLUMN_NOTE_ID + " = ? ",
                    new String[]{String.valueOf(idNote)});
        } catch (SQLiteException e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void deleteAllNotes() {
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            sqLiteDatabase.delete(Config.TABLE_NOTE, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_NOTE);

            if (count == 0)
                deleteStatus = true;

        } catch (SQLiteException e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public long getNumberOfNote() {
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_NOTE);
        } catch (SQLiteException e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return count;
    }
}