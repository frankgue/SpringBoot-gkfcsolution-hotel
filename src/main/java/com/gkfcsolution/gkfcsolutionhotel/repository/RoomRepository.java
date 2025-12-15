package com.gkfcsolution.gkfcsolutionhotel.repository;

import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created on 2025 at 14:45
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 14:45
 */
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();
}
