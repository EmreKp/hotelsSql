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

    public void validate(String checkIn, String checkOut, int pax) throws Exception {
        if(pax<1) {
            throw new Exception("Pax must be greater than zero");
        }
        Date checkInDate=new Date();
        Date checkOutDate=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            checkInDate=dateFormat.parse(checkIn);
            checkOutDate=dateFormat.parse(checkOut);
        } catch (Exception ex) {
            throw new Exception("Dates must be formatted in YYYY-MM-DD");
        }
        long checkInTime=checkInDate.getTime();
        long checkOutTime=checkOutDate.getTime();
        if(checkInTime>=checkOutTime) {
            throw new Exception("The checkout date must be after checkin.");
        }
    }
}
