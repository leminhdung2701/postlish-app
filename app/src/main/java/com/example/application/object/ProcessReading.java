package com.example.application.object;

public class ProcessReading {

    private Writing writing;
    private String userIdReading;

    public ProcessReading(Writing writing, String userId) {
        this.writing = writing;
        this.userIdReading= userId;
    }

    public void countReading() {
        writing.setTimes(writing.getTimes() + 1);
    }



}
