package com.example.application.object;

public class Rating{
    private float rating;
    private String userId;
    private String writingId;
    public Rating (){

    }
    public Rating (String a, String b, float c){
        userId = a;
        writingId = b;
        rating = c;
    }
    public String getUserId (){
        return userId;
    }
    public String getWritingId (){
        return writingId;
    }
    public float getRating(){
        return rating;
    }
    public void setUserId (String a){
        userId = a;
    }
    public void setWritingId (String b){
        writingId = b;
    }
    public void setRating (float c){
        rating = c;
    }
}
