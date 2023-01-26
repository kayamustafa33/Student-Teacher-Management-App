package com.example.kayit.faculty.notice;

public class NoticeData {
    public String title;
    public String image;
    public String date;
    public String time;
    public String key;

    public NoticeData(){
    }

    public NoticeData(String title, String image, String date, String time, String key) {
        //D
        this.title = title;
        this.image = image;
        this.date = date;
        this.time = time;
        this.key = key;
    }

    //verilere erişmek için getter ve setter kullanıldı
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
