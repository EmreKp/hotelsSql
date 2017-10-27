package com.example.sqlvericek.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;

@Entity
@Table
public class Room {
    @Id
    @GeneratedValue
    private Integer ID;

    private String room_code;
    private int allotment,max_pax;
    private float price;

    private Float discount;
    private Date date;

    protected Room(){}

    public Room(String code, int allot, float fiyat, String date, float disco, int pax) throws ParseException {
        this.room_code=code;
        this.allotment=allot;
        this.price=fiyat;
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        this.date=dateFormat.parse(date);
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
    public Float getDiscount(){
        return discount;
    }
}
