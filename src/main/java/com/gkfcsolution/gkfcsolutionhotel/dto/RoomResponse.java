package com.gkfcsolution.gkfcsolutionhotel.dto;

import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

/**
 * Created on 2025 at 15:13
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    private String  photo;
    private List<BookedRoom> bookings;

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoBytes, List<BookedRoom> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
        this.bookings = bookings;
    }
}
