package com.example.sqlvericek.Repository;

import com.example.sqlvericek.Model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {
    @Query(value = "SELECT * FROM inventories WHERE allotment>0 AND date>=?1 AND date<?2 AND max_pax>=?3",
            nativeQuery = true)
    List<Room> findAllRooms(Date checkIn, Date checkOut, int pax);
}
