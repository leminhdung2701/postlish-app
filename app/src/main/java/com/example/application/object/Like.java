package com.example.application.object;

public class Like {
    private String post_id;
    private String user_id;
    Like(){

    }

    public Like(String post_id, String user_id) {
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
