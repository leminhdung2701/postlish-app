package com.example.application.postmanage;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.database.ServerValue;

public class PostContent {

    private String postContentId;
    private String content;
    private Object timeStamp;

    public PostContent(String content){
        this.content =content;
    }


    public PostContent(String postContentId , String content) {
        this.content = content;
        this.postContentId=postContentId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public PostContent(){

    }

    public String getPostContentId() {
        return postContentId;
    }

    public void setPostContentId(String postContentId) {
        this.postContentId = postContentId;
    }

    public String getContent() {
        return content;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
