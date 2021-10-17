package com.example.dod;

public class reminder_model {

    String msg,time,date;
    String documentId;

    public reminder_model() {
    }

    public reminder_model(String msg, String time, String date) {
        this.msg = msg;
        this.time = time;
        this.date = date;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
