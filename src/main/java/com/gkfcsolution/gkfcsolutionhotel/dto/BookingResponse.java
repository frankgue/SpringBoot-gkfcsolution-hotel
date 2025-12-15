package com.gkfcsolution.gkfcsolutionhotel.dto;

import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created on 2025 at 15:14
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId ;
    private LocalDate checkInDate ;
    private LocalDate checkOutDate ;
//    private String guestName ;
    private String guestEmail ;
    private int numOfAdults ;
    private int numOfChildren ;
    private int totalNumbOfGuests ;
    private String  bookingConfirmationCode ;
    private Room room ;

    public BookingResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
