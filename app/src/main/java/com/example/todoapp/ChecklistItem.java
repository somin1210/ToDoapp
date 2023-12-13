package com.example.todoapp;

public class ChecklistItem {

    private String text;
    private boolean isChecked;
    private long checkedTime; // 체크박스 눌린 시간
    private long createdTime; // 아이템이 생성된 시간
    private long id;

    public ChecklistItem(String text) {
        this.text = text;
        this.isChecked = false;
        this.createdTime = System.currentTimeMillis(); // 아이템이 생성되는 시점의 시간을 저장
    }

    public ChecklistItem(String text, boolean isChecked, long createdTime) {
        this.text = text;
        this.isChecked = isChecked;
        this.createdTime = createdTime;
    }

    public ChecklistItem(long id, String text, boolean isChecked, long createdTime) {
        this.id = id;
        this.text = text;
        this.isChecked = isChecked;
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(long checkedTime) {
        this.checkedTime = checkedTime;
    }

    // createdTime에 대한 getter 메서드를 추가합니다.
    public long getCreatedTime() {
        return createdTime;
    }

    public long getId() {
        return id;
    }
}