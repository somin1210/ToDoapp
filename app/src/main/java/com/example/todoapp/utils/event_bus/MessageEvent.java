package com.example.todoapp.utils.event_bus;


public class MessageEvent {
    public static final String CREATE_NOTE = "CREATE_NOTE";
    public static final String UPDATE_NOTE = "UPDATE_NOTE";


    private String msg;
    public MessageEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
