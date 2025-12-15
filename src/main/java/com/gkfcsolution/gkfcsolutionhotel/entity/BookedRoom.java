package com.gkfcsolution.gkfcsolutionhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created on 2025 at 14:43
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 14/12/2025
 * @time 14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @Column(name = "check_In")
    private LocalDate checkInDate;
    @Column(name = "check_Out")
    private LocalDate checkOutDate;
    @Column(name = "guest_FullName")
    private String guestFullName;
    @Column(name = "guest_Email")
    private String guestEmail;
    @Column(name = "adults")
    private int numOfAdults;
    @Column(name = "children")
    private int numOfChildren;
    @Column(name = "total_guest")
    private int totalNumbOfGuests;
    @Column(name = "confirmation_Code", unique = true)
    private String bookingConfirmationCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumbOfGuests() {
        this.totalNumbOfGuests = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumbOfGuests();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumbOfGuests();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode.toUpperCase();
    }

    public BookedRoom(
            Long bookingId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            String bookingConfirmationCode
    ) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
