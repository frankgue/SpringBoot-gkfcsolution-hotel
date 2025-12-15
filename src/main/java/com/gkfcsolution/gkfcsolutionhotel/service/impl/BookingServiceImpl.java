package com.gkfcsolution.gkfcsolutionhotel.service.impl;

import com.gkfcsolution.gkfcsolutionhotel.dto.BookingResponse;
import com.gkfcsolution.gkfcsolutionhotel.dto.RoomResponse;
import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import com.gkfcsolution.gkfcsolutionhotel.entity.Room;
import com.gkfcsolution.gkfcsolutionhotel.exception.InvalidBookingRequestException;
import com.gkfcsolution.gkfcsolutionhotel.exception.ResourceNotFoundException;
import com.gkfcsolution.gkfcsolutionhotel.repository.BookingRepository;
import com.gkfcsolution.gkfcsolutionhotel.service.IBookingService;
import com.gkfcsolution.gkfcsolutionhotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2025 at 08:53
 * File: BookingServiceImpl.java.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 15/12/2025
 * @time 08:53
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;


    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            log.error("Check-in date must come before check-out date");
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }

        Room room = roomService.getRoomById(roomId);
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        } else {
            log.error("Room with id {} is not available for the selected dates", roomId);
            throw new InvalidBookingRequestException("Room with id " + roomId + " is not available for the selected dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        List<BookedRoom> bookings = bookingRepository.findAll();
//        List<BookedRoom> bookings = bookingRepository.findAllBooking();
        List<BookingResponse> bookingResponses = new ArrayList<>();

        for (BookedRoom bookedRoom : bookings) {
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);
            bookingResponses.add(bookingResponse);
        }

        return bookingResponses;
    }

    @Override
    public BookingResponse findByBookingConfirmationCode(String confirmationCode) {
        try {
            BookedRoom booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found for confirmation code: " + confirmationCode));
            BookingResponse bookingResponse = getBookingResponse(booking);
            return bookingResponse;
        } catch (ResourceNotFoundException ex) {
            log.error("Booking not found for confirmation code: {}", confirmationCode);
            throw new ResourceNotFoundException("Booking not found for confirmation code: " + confirmationCode);
        }
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room room = roomService.getRoomById(booking.getRoom().getId());

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .guestEmail(booking.getGuestEmail())
                .numOfAdults(booking.getNumOfAdults())
                .numOfChildren(booking.getNumOfChildren())
                .totalNumbOfGuests(booking.getTotalNumbOfGuests())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
//                .guestName(booking.getGuestFullName())
                .room(room)
                .build();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }


    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || (bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckOutDate()))
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())                        )
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))

                );
    }
}
