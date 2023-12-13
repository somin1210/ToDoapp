package com.example.todoapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity {

    private ChecklistAdapter adapter;
    private ArrayList<ChecklistItem> checklistItems;
    private ChecklistDatabaseHelper dbHelper; // ChecklistDatabaseHelper 인스턴스를 추가합니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        dbHelper = new ChecklistDatabaseHelper(this); // dbHelper를 초기화합니다.
        checklistItems = loadChecklistItems(); // 데이터베이스에서 체크리스트 아이템을 불러옵니다.
        adapter = new ChecklistAdapter(checklistItems, this::deleteChecklistItem, this::updateChecklistItem);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EditText editText = findViewById(R.id.editText);
        Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    addChecklistItem(text); // 데이터베이스에 체크리스트 아이템을 추가합니다.
                    editText.setText("");
                }
            }
        });
    }

    private ArrayList<ChecklistItem> loadChecklistItems() {
        ArrayList<ChecklistItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("checklist", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String text = cursor.getString(cursor.getColumnIndex("text"));
            boolean isChecked = cursor.getInt(cursor.getColumnIndex("isChecked")) != 0;
            long createdTime = cursor.getLong(cursor.getColumnIndex("createdTime"));
            items.add(new ChecklistItem(id, text, isChecked, createdTime));
        }
        cursor.close();

        return items;
    }

    private void addChecklistItem(String text) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("text", text);
        values.put("isChecked", 0);
        values.put("createdTime", System.currentTimeMillis());

        long id = db.insert("checklist", null, values);
        if (id == -1) {
            Log.e("ChecklistActivity", "Failed to insert item");
        } else {
            Log.d("ChecklistActivity", "Inserted item with ID: " + id); // 추가한 아이템의 ID를 로그로 출력합니다.
            checklistItems.add(new ChecklistItem(id, text, false, System.currentTimeMillis()));
            adapter.notifyDataSetChanged();
        }
    }

    private void updateChecklistItem(ChecklistItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isChecked", item.isChecked() ? 1 : 0);

        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        db.update("checklist", values, selection, selectionArgs);
    }

    private void deleteChecklistItem(ChecklistItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        int deletedRows = db.delete("checklist", selection, selectionArgs); // 삭제한 행의 수를 반환합니다.
        Log.d("ChecklistActivity", "Deleted rows: " + deletedRows); // 삭제한 행의 수를 로그로 출력합니다.

        // 아이템이 삭제된 후에 checklistItems에서 아이템을 직접 삭제하는 부분을 제거합니다.
    }
}

//        Button todayButton = findViewById(R.id.todayButton);
//        Button allButton = findViewById(R.id.allButton);
//        Button groupButton = findViewById(R.id.groupButton);

//        allButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // All 버튼이 눌렸을 때 수행할 동작을 작성합니다.
//            }
//        });
//
//        groupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Group 버튼이 눌렸을 때 수행할 동작을 작성합니다.
//            }
//        });
//
//        todayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.filterTodayItems();
//            }
//        });
