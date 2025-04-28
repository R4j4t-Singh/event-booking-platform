package com.example.event_booking_platform.service;

import com.example.event_booking_platform.entity.Seat;
import com.example.event_booking_platform.entity.Show;
import com.example.event_booking_platform.exception.EventNotFoundException;
import com.example.event_booking_platform.exception.ShowNotFoundException;
import com.example.event_booking_platform.repository.SeatRepository;
import com.example.event_booking_platform.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowRepository showRepository;

    public List<Seat> getSeats(Long showId) throws ShowNotFoundException {
        Show show = showRepository.findById(showId).orElseThrow(() -> new ShowNotFoundException("Show not found"));
        return seatRepository.findAllByShow(show);
    }
}
