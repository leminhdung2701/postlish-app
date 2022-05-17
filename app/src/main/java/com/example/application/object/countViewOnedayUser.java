package com.example.application.object;
import com.google.firebase.database.ServerValue;
public class countViewOnedayUser {
    private int count_day;
    private Object timestamp;
    public countViewOnedayUser(){

    }
    public countViewOnedayUser(int count_day, Object timestamp) {
        this.count_day = count_day;
        this.timestamp = timestamp;
    }

    public int getCount_day() {
        return count_day;
    }

    public void setCount_day(int count_day) {
        this.count_day = count_day;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
