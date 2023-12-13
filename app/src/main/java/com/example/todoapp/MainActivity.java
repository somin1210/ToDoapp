package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton, btnListNote, btnCreateNewNote, weatherButton, checklistbutton;
    public String readDay = null;
    public CalendarView calendarView;
    public TextView diaryTextView, textView2, textView3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        weatherButton = findViewById(R.id.weatherbutton);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickUrl(view);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                checkDay(year, month, dayOfMonth);
            }
        });

        // 체크리스트 화면 연결
        imageButton = findViewById(R.id.checklistbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ChecklistActivity.class);
                startActivity(intent1);
            }
        });


        // 타이머 화면 연결
        imageButton = findViewById(R.id.timerbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), timer.class);
                startActivity(intent2);
            }
        });

        //button view list note
        btnListNote = findViewById(R.id.btnListNote);
        btnListNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoListNoteActivity();
            }
        });

        //button create new note
        btnCreateNewNote = findViewById(R.id.btnCreateNewNote);
        btnCreateNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCreateNewNoteActivity();
            }
        });
    }

    private void gotoListNoteActivity() {
        startActivity(new Intent(MainActivity.this, ListNoteActivity.class));
    }

    private void gotoCreateNewNoteActivity() {
        startActivity(new Intent(MainActivity.this, CreateNewNoteActivity.class));
    }

    public void clickUrl(View view) {
        String url = "https://www.weather.go.kr/w/index.do";
        Log.d("MainActivity", "Opening URL: " + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
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
    }
}