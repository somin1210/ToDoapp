package com.example.todoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class record_check extends AppCompatActivity{
    private ListView listView;
    private DBhelper_timer dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_check);

        listView = findViewById(R.id.listView);
        dbHelper = new DBhelper_timer(this);

        // 데이터베이스로부터 모든 레코드를 가져오기
        Cursor cursor = dbHelper.getAllRecords();

        ArrayList<String> recordsList = new ArrayList<>();

        // Cursor에서 데이터를 읽어와 리스트에 추가
        while (cursor.moveToNext()) {
            String record = cursor.getString(cursor.getColumnIndex(DBhelper_timer.COLUMN_RECORD));
            String memo = cursor.getString(cursor.getColumnIndex(DBhelper_timer.COLUMN_MEMO));

            // 원하는 형식으로 데이터를 가공하여 리스트에 추가
            String displayText = "Record: " + record + "\nMemo: " + memo;
            recordsList.add(displayText);
        }

        // 리스트뷰에 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordsList);
        listView.setAdapter(adapter);
    }


}
