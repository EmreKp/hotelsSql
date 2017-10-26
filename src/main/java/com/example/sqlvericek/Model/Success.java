package com.example.sqlvericek.Model;

public class Success extends Result {
    public String roomCode;
    public Float price;
    public Success(String code,float price){
        this.roomCode=code;
        this.price=price;
    }
}
