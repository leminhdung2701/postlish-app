package com.example.application.postmanage;


import com.google.firebase.database.ServerValue;

public class Post {


    private String postKey;
    private String title;
    private String postContentId;
    private String picture;
    private String userId;
    private String userName;
    private String userPhoto;
    private String category_post;
    private Object timeStamp;



    public Post( String postContentId,String title, String picture, String userId,String userName,String userPhoto,String category_post) {
        this.title = title;
        this.postContentId = postContentId;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.userName = userName;
        this.category_post=category_post;
    }

    // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getCategory_post() {
        return category_post;
    }

    public void setCategory_post(String category_post) {
        this.category_post = category_post;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getPostContentId() {
        return postContentId;
    }

    public void setPostContentId(String postContentId) {
        this.postContentId = postContentId;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
