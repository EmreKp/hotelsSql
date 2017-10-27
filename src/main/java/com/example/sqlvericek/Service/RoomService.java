package com.example.sqlvericek.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.sqlvericek.Model.Msg;
import com.example.sqlvericek.Model.Result;
import com.example.sqlvericek.Model.Room;
import com.example.sqlvericek.Model.Success;
import com.example.sqlvericek.Repository.RoomRepository;
import com.example.sqlvericek.Validator.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    int status;

    @Autowired
    private DateValidator validator;

    @Autowired
    public RoomService(DateValidator validator){
        this.validator = validator;
    }

    public List<Result> getRooms(Date checkIn, Date checkOut, int pax) {
        List<Result> resultList=new ArrayList<>();
        List<Result> otherList=new ArrayList<>();
        List<Room> roomList = roomRepository.findAllRooms(checkIn,checkOut,pax);
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
            long checkInTime=checkIn.getTime();
            long checkOutTime=checkOut.getTime();
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
            status=200;
            otherList.add(msg);
            return otherList;
        }
        status=200;
        return resultList;
    }

    public int getStatus(){
        return status;
    }

}
