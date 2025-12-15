package com.gkfcsolution.gkfcsolutionhotel.controller;

import com.gkfcsolution.gkfcsolutionhotel.dto.BookingResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import com.gkfcsolution.gkfcsolutionhotel.exception.InvalidBookingRequestException;
import com.gkfcsolution.gkfcsolutionhotel.exception.ResourceNotFoundException;
import com.gkfcsolution.gkfcsolutionhotel.service.IBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 2025 at 10:21
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 15/12/2025
 * @time 10:21
 */
@RestController
@RequestMapping("/api/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @GetMapping("/allbookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping("/rooms/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest) {
        try {
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Booking created with confirmation code: " + confirmationCode);
        } catch (InvalidBookingRequestException ex) {
            log.error("Room not found for roomId: {}", roomId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found for roomId: " + roomId);
        }
    }

    @GetMapping("/confirmation/{confirmationcode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            return ResponseEntity.ok(bookingService.findByBookingConfirmationCode(confirmationCode));
        } catch (ResourceNotFoundException ex) {
            log.error("Booking not found for confirmation code: {}", confirmationCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found for confirmation code: " + confirmationCode);
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
