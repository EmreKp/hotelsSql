package com.example.sqlvericek.Validator;

import com.example.sqlvericek.Model.Msg;
import com.example.sqlvericek.Model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Validator {

    public List<Date> dateValidator(String checkIn, String checkOut){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date checkInDate=new Date();
        Date checkOutDate=new Date();
        String errMsg;
        List<Date> dateList=new ArrayList<>();
        try {
            checkInDate = dateFormat.parse(checkIn);
            checkOutDate = dateFormat.parse(checkOut);

        } catch (ParseException ex) {
            errMsg="Dates must be formatted in YYYY-MM-DD";
        }
        dateList.add(checkInDate);
        dateList.add(checkOutDate);
        return dateList;
}
