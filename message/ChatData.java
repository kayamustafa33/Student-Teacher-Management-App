package com.example.kayit.message;

public class ChatData {

    private String text,from;

    public ChatData() {
    }

    //constructor initialize edildi
    public ChatData(String text, String from) {
        this.text = text;
        this.from = from;
    }

    //getter - setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
