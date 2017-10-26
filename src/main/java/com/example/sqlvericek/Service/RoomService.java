package com.example.sqlvericek.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.sqlvericek.Model.Msg;
import com.example.sqlvericek.Model.Result;
import com.example.sqlvericek.Model.Room;
import com.example.sqlvericek.Model.Success;
import com.example.sqlvericek.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    boolean error=false;

    public List<Result> getRooms(String checkIn, String checkOut, int pax) {
        List<Result> resultList=new ArrayList<>();
        List<Result> otherList=new ArrayList<>();
        //fetch dates from params
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date checkInDate;
        Date checkOutDate;
        try {
            checkInDate = df.parse(checkIn);
            checkOutDate = df.parse(checkOut);
        } catch (ParseException ex) {
            Result err = new Msg("Dates must be formatted in YYYY-MM-DD");
            this.error=true;
            otherList.add(err);
            return otherList;
        }
        //it gives zero prices if dates are equal or in after out
        if (checkInDate.after(checkOutDate) || checkInDate.equals(checkOutDate)) {
            Result err=new Msg("The checkout date is not after checkin date.");
            this.error=true;
            otherList.add(err);
            return otherList;
        }

        // Databaseden allotment >0 and max_pax <= pax, and date between checkIn and checkout -1
        // Room code'a göre grupla (hash map)
        // hash map'i gez, count < checkout - checkin (gün sayısı, gece sayısı) küçük olanları ele
        // elimizde sadece uygun odalar kalmış olacak.
        // hash map'i tekrar gez, fiyatlamaları uygula, result'ı dön

        List<Room> roomList = roomRepository.findAllRooms(checkInDate,checkOutDate,pax);
        //group all by map
        Map<String, List<Room>> resultMap = new HashMap<>();
        for (Room room : roomList) {
            //check if key exists
            if (resultMap.containsKey(room.getCode())) {
                resultMap.get(room.getCode()).add(room);
            } else {
                List<Room> list = new ArrayList<>();
                list.add(room);
                resultMap.put(room.getCode(), list);
            }
        }

        //iterate map
        for (Iterator<Map.Entry<String,List<Room>>> it=resultMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String,List<Room>> entry=it.next();
            List<Room> list=entry.getValue();
            long listCount=list.size(); //check if dates between in and out are full
            long checkInTime=checkInDate.getTime();
            long checkOutTime=checkOutDate.getTime();
            long plannedDays=(checkOutTime-checkInTime)/1000/60/60/24;
            if (listCount<plannedDays) {
                it.remove();
            }
        }

        //calculate total price and return the list
        for (Map.Entry<String, List<Room>> entry : resultMap.entrySet()) {
            Float total = 0.0f;
            List<Room> list = entry.getValue();
            for (Room room : list) {
                float price = room.getPrice();
                Float discount = room.getDiscount();
                if (discount != null) {
                    price -= price * discount / 100;
                }
                total += price;
            }
            Result result = new Success(entry.getKey(), total);
            resultList.add(result);
        }

        if (resultList.isEmpty()) {
            Result msg = new Msg("Rooms not found");
            otherList.add(msg);
            return otherList;
        }
        return resultList;
    }

    public boolean getError(){
        return error;
    }
}
