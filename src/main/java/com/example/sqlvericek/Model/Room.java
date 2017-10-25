package com.example.sqlvericek.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Room {
    @Id
    @GeneratedValue
    private int ID;

    private String room_code;
    private int allotment,max_pax;
    private float price,discount;
    private Date date;

    protected Room(){}

    public Room(String code, int allot, float fiyat, String tarih, float disco, int pax){
        this.room_code=code;
        this.allotment=allot;
        this.price=fiyat;
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date=df.parse(tarih);
        }
        catch (ParseException ex){

        }
        this.discount=disco;
        this.max_pax=pax;
    }
    //start of getters
    public String getCode(){
        return room_code;
    }
    public int getAllotment(){
        return allotment;
    }
    public Date getDate() {
        return date;
    }
    public int getMaxPax(){
        return max_pax;
    }
    public float getPrice(){
        return price;
    }
    public float getDiscount(){
        return discount;
    }
}
