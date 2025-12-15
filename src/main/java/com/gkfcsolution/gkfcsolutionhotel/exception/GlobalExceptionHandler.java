package com.gkfcsolution.gkfcsolutionhotel.exception;

import com.gkfcsolution.gkfcsolutionhotel.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Created on 2025 at 16:45
 * File: null.java
 * Project: springboot-movies-bookinks
 *
 * @author Frank GUEKENG
 * @date 12/12/2025
 * @time 16:45
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorDetails> handleBookingException(BookingException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "BOOKING_NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ErrorDetails> handleRoomException(RoomException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "ROOM_NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Handle Generic exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), webRequest.getDescription(false), "INTERNAL_SERVER_ERROR");

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
