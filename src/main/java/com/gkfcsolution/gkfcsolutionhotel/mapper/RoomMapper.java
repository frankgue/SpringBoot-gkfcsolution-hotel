package com.gkfcsolution.gkfcsolutionhotel.mapper;

import com.gkfcsolution.gkfcsolutionhotel.dto.RoomResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import com.gkfcsolution.gkfcsolutionhotel.service.IRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

/**
 * Created on 2025 at 17:15
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 17:15
 */
@Slf4j
public class RoomMapper {


    public static RoomResponse toRoomResponse(Room room) {
        try {
            return RoomResponse.builder()
                    .id(room.getId())
                    .roomType(room.getRoomType())
                    .roomPrice(room.getRoomPrice())
                    .isBooked(room.isBooked())
                    .photo(room.getPhoto() != null ?Base64.getEncoder().encodeToString(room.getPhoto().getBytes(1, (int) room.getPhoto().length())) : null)
                    .build();
        } catch (SQLException e) {
            log.error("Error retrieving photo");
            throw new RuntimeException(e);
        }

    }

    public static Room toRoom(RoomResponse roomResponse) {
        Room room = new Room();
        room.setId(roomResponse.getId());
        room.setRoomType(roomResponse.getRoomType());
        room.setRoomPrice(roomResponse.getRoomPrice());
        room.setBooked(roomResponse.isBooked());
//        room.setPhoto(roomResponse.getPhoto() != null ? Base64.getDecoder().decode(roomResponse.getPhoto()) : null);
        return room;
    }
}
