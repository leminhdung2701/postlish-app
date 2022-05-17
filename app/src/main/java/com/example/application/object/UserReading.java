package com.example.application.object;
import java.util.ArrayList;
public class UserReading{
    private Writing writing;
    private String userId;
    ArrayList <Writing> listReading = new ArrayList <Writing>();
    public UserReading (){
        
    }
    public UserReading (Writing writ, String a){
        writing = writ;
        userId = a;
    }
    public void setWriting (Writing writ){
        writing = writ;
    }
    public void setUserId (String a){
        userId = a;
    }
    public Writing getWriting (){
        return writing;
    }
    public String getUserId(){
        return userId;
    }
    public void addListReading (Writing writ){
        listReading.add (writ);
    }
}
//test
