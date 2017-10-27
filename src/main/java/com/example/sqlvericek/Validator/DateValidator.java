package com.example.sqlvericek.Validator;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DateValidator {
    private boolean error;
    private String message;


    public List<Date> validate(String checkIn, String checkOut) throws Exception {
        List<Date> dateList=new ArrayList<>();
        Date checkInDate=new Date();
        Date checkOutDate=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            checkInDate = dateFormat.parse(checkIn);
            checkOutDate = dateFormat.parse(checkOut);
        } catch (ParseException ex) {
            throw new Exception("Dates must be formatted in YYYY-MM-DD");
        }
        long checkInTime=checkInDate.getTime();
        long checkOutTime=checkOutDate.getTime();
        if(checkInTime>=checkOutTime) {
            throw new Exception("The checkout date must be after checkin.");
        }
        dateList.add(checkInDate);
        dateList.add(checkOutDate);
        return dateList;
    }
}
