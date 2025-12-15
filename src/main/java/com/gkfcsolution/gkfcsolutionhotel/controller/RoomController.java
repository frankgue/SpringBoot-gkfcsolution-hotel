package com.gkfcsolution.gkfcsolutionhotel.controller;

import com.gkfcsolution.gkfcsolutionhotel.dto.RoomResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import com.gkfcsolution.gkfcsolutionhotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

/**
 * Created on 2025 at 14:44
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 14:44
 */
@RestController
@RequestMapping("/api/rooms")
@Slf4j
@RequiredArgsConstructor
public class RoomController {
    private final IRoomService roomService;

    @PostMapping("/add/newroom")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo")MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
            ) {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse roomResponse = RoomResponse.builder()
                .id(savedRoom.getId())
                .roomType(savedRoom.getRoomType())
                .roomPrice(savedRoom.getRoomPrice())
                .build();
        return new ResponseEntity<>(roomResponse, HttpStatus.CREATED);
    }
    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestParam("photo")MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
            ) {
        byte[] photoBytes = new byte[0];
        try {
            photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);

            Blob photoBlob = photoBytes != null ? new SerialBlob(photoBytes) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Room updatedRoom = roomService.updateRoom(roomId, photoBytes, roomType, roomPrice);
        RoomResponse roomResponse = roomService.getRoomResponse(updatedRoom);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/getallrooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @GetMapping("/room/types")
    public ResponseEntity<List<String>> getRoomTypes() {
//        List<String> roomTypes = List.of("Single", "Double", "Suite", "Deluxe", "Family", "Presidential");
        return ResponseEntity.ok(roomService.getAllRoomTypes());
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
