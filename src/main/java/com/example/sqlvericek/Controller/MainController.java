package com.example.sqlvericek.Controller;

import java.util.*;

import com.example.sqlvericek.Model.*;
import com.example.sqlvericek.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private RoomService roomService;

    @Autowired
    public MainController(RoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping(value = "/hotels", produces = "application/json")
    public ResponseEntity<List<Result>> hotelList(
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam("pax") int pax
    ) {
        List<Result> list=this.roomService.getRooms(checkIn, checkOut, pax);
        int status=this.roomService.getStatus();
        return ResponseEntity.status(status).body(list);
    }
}
