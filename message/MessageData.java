package com.example.kayit.message;

public class MessageData {

    private String email,name,password;

    public MessageData(){

    }

    //constructor initialize edildi
    public MessageData(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    //getter - setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
