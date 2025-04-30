package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.dto.SeatResponse;
import com.example.event_booking_platform.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/seats")
public class PublicSeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/show/{showId}")
    public ResponseEntity<?> getSeats(@PathVariable Long showId) {
        try {
            List<SeatResponse> seats = seatService.getSeats(showId);
            return new ResponseEntity<>(seats, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
