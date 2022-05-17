package com.example.application.object;
import com.google.firebase.database.ServerValue;
public class CountView {
    private int count_day;
    private Object timestamp;
    public CountView(){

    }

    public CountView(int count) {
        this.count_day = count;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public int getCount() {
        return count_day;
    }

    public void setCount(int count) {
        this.count_day = count;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
