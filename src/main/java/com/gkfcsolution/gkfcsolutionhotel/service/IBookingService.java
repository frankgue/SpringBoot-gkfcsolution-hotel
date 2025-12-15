package com.gkfcsolution.gkfcsolutionhotel.service;

import com.gkfcsolution.gkfcsolutionhotel.dto.BookingResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;

import java.util.List;

/**
 * Created on 2025 at 08:52
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 15/12/2025
 * @time 08:52
 */
public interface IBookingService {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);


    List<BookingResponse> getAllBookings();
    BookingResponse findByBookingConfirmationCode(String confirmationCode);
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);
}
