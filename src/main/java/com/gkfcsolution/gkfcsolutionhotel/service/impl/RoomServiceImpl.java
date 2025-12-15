package com.gkfcsolution.gkfcsolutionhotel.service.impl;

import com.gkfcsolution.gkfcsolutionhotel.dto.BookingResponse;
import com.gkfcsolution.gkfcsolutionhotel.dto.RoomResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import com.gkfcsolution.gkfcsolutionhotel.exception.InternalServerException;
import com.gkfcsolution.gkfcsolutionhotel.exception.PhotoRetrievalException;
import com.gkfcsolution.gkfcsolutionhotel.exception.ResourceNotFoundException;
import com.gkfcsolution.gkfcsolutionhotel.mapper.RoomMapper;
import com.gkfcsolution.gkfcsolutionhotel.repository.RoomRepository;
import com.gkfcsolution.gkfcsolutionhotel.service.IBookingService;
import com.gkfcsolution.gkfcsolutionhotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created on 2025 at 14:47
 * File: RoomServiceImpl.java.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 14:47
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {
    private final RoomRepository roomRepository;
    private final IBookingService bookingService;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) {
        Room room = Room.builder()
                .roomType(roomType)
                .roomPrice(roomPrice)
                .build();

        if (!file.isEmpty()) {
            try {
                byte[] photoBytes = file.getBytes();
                Blob photoBlob = new SerialBlob(photoBytes);
                room.setPhoto(photoBlob);
            } catch (Exception e) {
                log.error("Error while saving room photo: {}", e.getMessage());
            }
        }

        return roomRepository.save(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room : rooms) {
            byte[] photoBytes = getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse roomResponse = RoomResponse.builder()
                        .id(room.getId())
                        .roomType(room.getRoomType())
                        .roomPrice(room.getRoomPrice())
                        .isBooked(room.isBooked())
                        .photo(base64Photo)
                        .build();
                roomResponses.add(roomResponse);
            }
        }

        return roomRepository.findAll().stream()
                .map(RoomMapper::toRoomResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isEmpty()) {
            throw new ResourceNotFoundException("Room not found for id: " + roomId);
        }

        Blob photoBlob = roomOptional.get().getPhoto();
        if (photoBlob != null) {
            try {
                int blobLength = (int) photoBlob.length();
                return photoBlob.getBytes(1, blobLength);
            } catch (Exception e) {
                log.error("Error while retrieving room photo: {}", e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id : " + roomId));
        roomRepository.delete(room);
    }

    @Override
    public Room updateRoom(Long roomId, byte[] photoBytes, String roomType, BigDecimal roomPrice) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id : " + roomId));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException e) {
                log.error("Error while updating room photo: {}", e.getMessage());
                throw new InternalServerException("Error while updating room photo");
            }
        }
        return roomRepository.save(room);
    }


    @Override
    public RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookedRoom> bookingInfo = bookings.stream()
                .map(booking -> new BookedRoom(
                        booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getBookingConfirmationCode()
                )).collect(Collectors.toList());

        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();

        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                log.error("Error retrieving photo");
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }

        return new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isBooked(),
                photoBytes,
                bookingInfo
        );
    }

    @Override
    public Room getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id : " + roomId));

        return room;
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }

}