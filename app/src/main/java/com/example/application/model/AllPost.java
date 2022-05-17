package com.example.application.model;


import com.example.application.postmanage.Post;

import java.util.List;

public class AllPost {

    String title;
    Integer id;
    private List<Post> list = null;

    public AllPost(Integer id, String title, List<Post> list) {
        this.title = title;
        this.id = id;
        this.list = list;
    }

    public List<Post> getPostList() {
        return list;
    }

    public void setPostList(List<Post> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer Id) {
        this.id = Id;
    }
}
