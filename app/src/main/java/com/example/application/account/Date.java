package com.example.application.account;

public class Date {
    public int day = 1;
    public int month =1;
    public int year =2001;
    public Date() {

    }
    public Date(int dday, int mmonth, int yyear) {
        this.day = dday;
        this.month= mmonth;
        this.year = yyear;
    }
    public String toString(){
        return day + "/" + month +"/" + year;
    }
}
