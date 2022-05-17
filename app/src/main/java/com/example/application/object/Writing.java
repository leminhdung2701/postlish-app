package com.example.application.object;
public class Writing {

    private String writingId;
    private String userId;
    private double times;
    private float rating;
    private boolean mode; //int is better

    protected Writing(){
        writingId = "UnRegistered";
        userId = "UnRegistered";
        rating = 0;
        times =0;
        mode = false;
    }
    protected Writing (String a, String b, float c, boolean e){
        userId = b;
        writingId = a;
        rating = c;
        mode = e;
        times =0;
    }

    public String getWritingId() {
        return writingId;
    }

    public void setWritingId(String writingId) {
        this.writingId = writingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTimes() {
        return times;
    }

    public void setTimes(double times) {
        this.times = times;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}