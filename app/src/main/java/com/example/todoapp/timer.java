package com.example.todoapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class timer extends AppCompatActivity {

    private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn, RCbtn;
    private TextView mTimeTextView, mRecordTextView;
    private Thread timeThread = null;
    private Boolean isRunning = true;
    private View Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#4ea1d3"));

        }
        mStartBtn = (Button) findViewById(R.id.btn_start);
        mStopBtn = (Button) findViewById(R.id.btn_stop);
        mRecordBtn = (Button) findViewById(R.id.btn_record);
        mPauseBtn = (Button) findViewById(R.id.btn_pause);
        mTimeTextView = (TextView) findViewById(R.id.timeView);
        mRecordTextView = (TextView) findViewById(R.id.recordView);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mStopBtn.setVisibility(View.VISIBLE);
                mRecordBtn.setVisibility(View.VISIBLE);
                mPauseBtn.setVisibility(View.VISIBLE);

                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mRecordBtn.setVisibility(View.GONE);
                mStartBtn.setVisibility(View.VISIBLE);
                mPauseBtn.setVisibility(View.GONE);
                mRecordTextView.setText("");
                timeThread.interrupt();
            }
        });

        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordTextView.setText(mRecordTextView.getText() + mTimeTextView.getText().toString() + "\n");
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning) {
                    mPauseBtn.setText("일시정지");
                } else {
                    mPauseBtn.setText("시작");
                }
            }
        });

        DBhelper_timer dbHelper = new DBhelper_timer(this);
        Button saveButton = (Button) findViewById(R.id.ok2);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recordText = mRecordTextView.getText().toString(); // recordView의 내용 가져오기
                String memoText = ((EditText) findViewById(R.id.memo_txt1)).getText().toString(); // memo_txt의 내용 가져오기

                // DB에 데이터 저장
                boolean isInserted = dbHelper.insertData(recordText, memoText);

                if (isInserted) {
                    Toast.makeText(timer.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(timer.this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button = findViewById(R.id.RCbtn);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(), record_check.class);

                startActivity(intent1);
            }
        });



    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
            if (result.equals("00:01:15:00")) {
                Toast.makeText(timer.this, "1분 15초가 지났습니다.", Toast.LENGTH_SHORT).show();
            }
            mTimeTextView.setText(result);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                mTimeTextView.setText("");
                                mTimeTextView.setText("00:00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }
}