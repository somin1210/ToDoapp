package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton;
    public String readDay = null;
    public CalendarView calendarView;
    public TextView diaryTextView, textView2, textView3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                checkDay(year, month, dayOfMonth);
            }
        });

        // 타이머 화면 연결
        imageButton = findViewById(R.id.timerbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(), timer.class);

                startActivity(intent1);
            }
        });
    }

    public void checkDay(int cYear, int cMonth, int cDay) {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;

        try {
            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            String str = new String(fileData);

            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            textView3.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

}}
