package com.example.sqlvericek.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.sqlvericek.Model.Msg;
import com.example.sqlvericek.Model.Result;
import com.example.sqlvericek.Model.Room;
import com.example.sqlvericek.Model.Success;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private JdbcTemplate jdbcTemp;

    @Autowired
    public RoomService(JdbcTemplate jdbcTemp) {
        this.jdbcTemp = jdbcTemp;
    }

    public List<Result> getRooms(String checkIn, String checkOut, int pax) {
        List<Result> rsList=new ArrayList<>();
        List<Result> other=new ArrayList<>();
        //fetch dates from params
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dIn = new Date();
        Date dOut = dIn;
        try {
            dIn = df.parse(checkIn);
            dOut = df.parse(checkOut);
        } catch (ParseException ex) {
            Result err = new Msg("Dates must be formatted in YYYY-MM-DD");
            other.add(err);
            return other;
        }
        //it gives zero prices if dates are equal or in after out
        if (dIn.after(dOut) || dIn.equals(dOut))
            return rsList;
        List<Room> roomList = new ArrayList<Room>();
        roomList.addAll(jdbcTemp.query("SELECT * FROM tabalo",
                (rs, rowNum) -> new Room(rs.getString("room_code"), rs.getInt("allotment"),
                        rs.getFloat("price"), rs.getString("date"), rs.getFloat("discount"), rs.getInt("max_pax")))
                );
        //group all by map
        Map<String, List<Room>> rsMap = new HashMap<>();
        for (Room room : roomList) {
            //check if key exists
            if (rsMap.containsKey(room.getCode())) {
                rsMap.get(room.getCode()).add(room);
            } else {
                List<Room> list = new ArrayList<>();
                list.add(room);
                rsMap.put(room.getCode(), list);
            }
        }
        //calculate total price and return the list
        for (Map.Entry<String, List<Room>> entry : rsMap.entrySet()) {
            float total = 0;
            List<Room> list = entry.getValue();
            for (Room room : list) {
                Date date = room.getDate();
                float price = room.getPrice();
                float discount = room.getDiscount();
                if ((date.equals(dIn) || date.after(dIn)) && date.before(dOut)) {
                    if (room.getAllotment() == 0 || room.getMaxPax() < pax) {
                        total = -1;
                        break;
                    }
                    if (discount != 0) price -= price * discount / 100;
                    total += price;
                }
            }
            //find if last date is before than expected
            list.sort(Comparator.comparing(Room::getDate));
            //lastly check first & last dates are equal
            Date firstDate = list.get(0).getDate();
            Date lastDate = list.get(entry.getValue().size() - 1).getDate();
            if (firstDate.after(dIn) || lastDate.before(dOut))
                total = -1;
            Result result = new Success(entry.getKey(), total);
            if (total != -1)
                rsList.add(result);
        }
        if (rsList.isEmpty()) {
            Result msg = new Msg("Rooms not found");
            other.add(msg);
            return other;
        }
        return rsList;
    }
}
