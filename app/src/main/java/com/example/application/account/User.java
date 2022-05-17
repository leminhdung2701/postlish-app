package com.example.application.account;

public class User {
    private String id;
    private String name;
    private String gmail;
    private String date = "01/01/2001";
    private boolean gender = false;
    private int role ;
    private String photoUrl;
    public User(String id, String name, String gmail, String date, boolean gender,int role) {
        this.id = id;
        this.name = name;
        this.gmail = gmail;
        this.date = date.toString();
        this.gender = gender;
        this.role = role;
    }
    public User(){

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
