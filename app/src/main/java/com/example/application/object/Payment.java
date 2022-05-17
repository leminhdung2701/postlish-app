package com.example.application.object;

public class Payment {

    String userID = "abc0165";
    private double balance = 0;
    public Payment(){

    }
    public Payment(String userID){
        this.userID = userID;
        balance = 0;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount){
        balance=balance+amount;
    }

    public void withdraw( double amount){
        balance=balance-amount;
    }

    public void transfer(double amount, Payment other){
        withdraw(amount);
        other.deposit(amount);
    }

}
