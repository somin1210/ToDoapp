package com.example.todoapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.todoapp.R;

import androidx.annotation.NonNull;


public class AlertTwoActionDialog extends Dialog {

    private String content;
    private String title;
    private String textAction;
    private OnClickItemListener onClickItemListener = null;

    public AlertTwoActionDialog(@NonNull Context context, String title, String content, String textAction, OnClickItemListener onClickItemListener) {
        super(context);
        this.title = title;
        this.content = content;
        this.textAction = textAction;
        this.onClickItemListener = onClickItemListener;
    }

    public AlertTwoActionDialog(@NonNull Context context, String title, String content) {
        super(context);
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(onClickItemListener == null);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.dialog_alert_two_action);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView btnCancel = findViewById(R.id.btnCancel);
        TextView btnOk = findViewById(R.id.btnOk);


        tvTitle.setText(title);
        tvContent.setText(content);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if (!textAction.isEmpty()) btnOk.setText(textAction);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null) onClickItemListener.onClickAction();
                dismiss();
            }
        });
    }

    public interface OnClickItemListener {
        void onClickAction();
    }
}
