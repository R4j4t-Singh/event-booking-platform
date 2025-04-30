package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.dto.BookingRequest;
import com.example.event_booking_platform.entity.Booking;
import com.example.event_booking_platform.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> bookSeats(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.bookSeats(request);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getbookings() {
        List<Booking> bookings = bookingService.getBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
