package com.example.todoapp;

public class Listrecord {
    private int id;
    private String record;
    private String memo;

    public Listrecord(int id, String record, String memo) {
        this.id = id;
        this.record = record;
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
