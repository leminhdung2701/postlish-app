package com.example.application.object;

public class Reading {

    private String userIdReading;
    private boolean mode;

    public Reading(String userIdReading ){
        this.userIdReading = userIdReading;
        mode = false;
    }

    public String getUserIdReading() {
        return userIdReading;
    }

    public void setUserIdReading(String userIdReading) {
        this.userIdReading = userIdReading;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }


}
