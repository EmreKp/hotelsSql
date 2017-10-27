package com.example.sqlvericek.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.sqlvericek.Error.DateParseError.DateParseError;
import com.example.sqlvericek.Error.ErrorsTypeModel.ErrorType;
import com.example.sqlvericek.Error.ErrorsTypeModel.ErrorsType;
import com.example.sqlvericek.Error.PaxError.ZeroPaxError;
import com.example.sqlvericek.Error.WrongDates.WrongDatesError;
import com.example.sqlvericek.Exception.WrongDatesException;
import com.example.sqlvericek.Exception.ZeroPaxException;
import com.example.sqlvericek.Model.*;
import com.example.sqlvericek.Service.RoomService;
import com.example.sqlvericek.Validator.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {
    private RoomService roomService;
    private DateValidator validator;

    @Autowired
    public MainController(RoomService roomService,DateValidator validator) {
        this.roomService = roomService;
        this.validator=validator;
    }

    @RequestMapping(value = "/hotels", produces = "application/json")
    public List<Result> hotelList (
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam("pax") int pax
    ) throws WrongDatesException,ParseException,ZeroPaxException {
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date checkInDate=dateFormat.parse(checkIn);
        Date checkOutDate=dateFormat.parse(checkOut);
        //automatically throws parse exception
        long checkInTime=checkInDate.getTime();
        long checkOutTime=checkOutDate.getTime();
        if(checkInTime>=checkOutTime) {
            throw new WrongDatesException("The checkout date must be after checkin.");
        }
        if(pax<1){
            throw new ZeroPaxException("Pax must be greater than 0.");
        }
        List<Result> list=this.roomService.getRooms(checkInDate, checkOutDate, pax);
        //int status=this.roomService.getStatus();
        return list;
    }

    @ExceptionHandler(WrongDatesException.class)
    public ResponseEntity<WrongDatesError> handleWrongDatesError(WrongDatesException wrex) {
        WrongDatesError wrongDatesError=new WrongDatesError();
        wrongDatesError.setErrors(responseErrorsType(wrex.getMessage()));
        return ResponseEntity.status(400).body(wrongDatesError);
    }

    @ExceptionHandler(ZeroPaxException.class)
    public ResponseEntity<ZeroPaxError> handleZeroPaxError(ZeroPaxException zpex) {
        ZeroPaxError zeroPaxError=new ZeroPaxError();
        zeroPaxError.setErrors(responseErrorsType(zpex.getMessage()));
        return ResponseEntity.status(400).body(zeroPaxError);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<DateParseError> handleDateParseError(ParseException pex){
        DateParseError dateParseError=new DateParseError();
        dateParseError.setErrors(responseErrorsType(pex.getMessage()));
        return ResponseEntity.status(400).body(dateParseError);
    }

    private ErrorsType responseErrorsType(String message){
        ErrorsType errorsType = new ErrorsType();
        ErrorType errorType = new ErrorType();
        errorType.setType(message);
        errorsType.setError(errorType);
        return errorsType;
    }
}
