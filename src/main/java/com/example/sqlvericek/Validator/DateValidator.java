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


    public void validate(Date checkIn, Date checkOut, int pax) throws Exception {
        if(pax<1) {
            throw new Exception("Pax must be greater than zero");
        }
        List<Date> dateList=new ArrayList<>();
        long checkInTime=checkIn.getTime();
        long checkOutTime=checkOut.getTime();
        if(checkInTime>=checkOutTime) {
            throw new Exception("The checkout date must be after checkin.");
        }
    }
}
