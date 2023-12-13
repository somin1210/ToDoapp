package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {

    private ArrayList<ChecklistItem> checklistItems;
    private Consumer<ChecklistItem> onItemDelete; // 삭제 이벤트 처리 메소드를 저장할 변수를 추가합니다.
    private Consumer<ChecklistItem> onItemUpdate; // 추가: 체크 상태 업데이트 이벤트 처리 메소드를 저장할 변수를 추가합니다.

    public ChecklistAdapter(ArrayList<ChecklistItem> checklistItems, Consumer<ChecklistItem> onItemDelete, Consumer<ChecklistItem> onItemUpdate) {
        this.checklistItems = checklistItems;
        this.onItemDelete = onItemDelete; // 삭제 이벤트 처리 메소드를 저장합니다.
        this.onItemUpdate = onItemUpdate; // 추가: 체크 상태 업데이트 이벤트 처리 메소드를 저장합니다.
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChecklistItem currentItem = checklistItems.get(position);

        // OnCheckedChangeListener를 잠시 해제
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.text.setText(currentItem.getText());

//        if (currentItem.isChecked()) {
//            long duration = System.currentTimeMillis() - currentItem.getCreatedTime();
//            String durationText = formatDuration(duration);
//            holder.timeText.setText(" (" + durationText + ")");
//            holder.timeText.setTextColor(Color.parseColor("#808080")); // 체크됐을 때 흐린 색상 설정
//        } else {
//            holder.timeText.setText(""); // 체크되지 않았을 때 시간 표시 텍스트 제거
//        }

        holder.checkBox.setChecked(currentItem.isChecked());

        // 체크리스트 아이템 생성 시간을 날짜 형식으로 변환합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String createdDate = sdf.format(new Date(currentItem.getCreatedTime()));
        holder.timeText.setText(createdDate);

        // 체크 상태 설정 후에 OnCheckedChangeListener를 다시 설정
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentItem.setChecked(isChecked);
                if (isChecked) {
                    currentItem.setCheckedTime(System.currentTimeMillis());
                }
                onItemUpdate.accept(currentItem); // 변경된 체크 상태를 데이터베이스에 업데이트합니다.
                notifyItemChanged(holder.getAdapterPosition()); // 변경된 아이템만 갱신
            }
        });

        // 삭제 버튼 클릭 이벤트
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && position < checklistItems.size()) { // 아이템이 유효한 위치에 있는지 확인합니다.
                    synchronized (this) {
                        ChecklistItem item = checklistItems.get(position);
                        onItemDelete.accept(item); // 삭제 이벤트를 처리합니다.
                        checklistItems.remove(position);
                    }
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (checklistItems != null ? checklistItems.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public CheckBox checkBox;
        public Button deleteButton; // 삭제 버튼 추가
        TextView timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            checkBox = itemView.findViewById(R.id.checkBox);
            timeText = itemView.findViewById(R.id.timeText); // 시간 표시 TextView 초기화
            deleteButton = itemView.findViewById(R.id.deleteButton); // 삭제 버튼 찾기
        }
    }

    private String formatDuration(long durationMillis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
        long hours = TimeUnit.MINUTES.toHours(minutes);

        minutes = minutes - TimeUnit.HOURS.toMinutes(hours); // 남은 분 계산

        return hours + "h " + minutes + "m";
    }

    // ChecklistAdapter 클래스에 아래 메소드를 추가합니다.
    public void filterTodayItems() {
        long now = System.currentTimeMillis();
        long oneDayMillis = 24 * 60 * 60 * 1000; // 하루를 밀리초로 변환

        Iterator<ChecklistItem> iterator = checklistItems.iterator();
        while (iterator.hasNext()) {
            ChecklistItem item = iterator.next();
            if (now - item.getCreatedTime() >= oneDayMillis) {
                iterator.remove();
            }
        }

        notifyDataSetChanged();
    }
}
