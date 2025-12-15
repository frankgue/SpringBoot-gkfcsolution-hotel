package com.gkfcsolution.gkfcsolutionhotel.repository;

import com.gkfcsolution.gkfcsolutionhotel.entity.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created on 2025 at 10:22
 * File: null.java
 * Project: gkfcsolution-hotel
 *
 * @author Frank GUEKENG
 * @date 15/12/2025
 * @time 10:22
 */
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

    List<BookedRoom> findByRoomId(Long roomId);
   Optional <BookedRoom> findByBookingConfirmationCode(String confirmationCode);

}
