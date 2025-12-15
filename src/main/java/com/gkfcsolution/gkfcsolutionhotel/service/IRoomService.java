package com.gkfcsolution.gkfcsolutionhotel.service;

import com.gkfcsolution.gkfcsolutionhotel.dto.RoomResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

/**
 * Created on 2025 at 14:46
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 14:46
 */
public interface IRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);

    List<RoomResponse> getAllRooms();

    List<String> getAllRoomTypes();
    byte[] getRoomPhotoByRoomId(Long roomId);
    void deleteRoom(Long roomId);
    RoomResponse getRoomResponse(Room room);
    Room getRoomById(Long roomId);
    Room updateRoom(Long roomId, byte[] photoBytes, String roomType, BigDecimal roomPrice);
}
