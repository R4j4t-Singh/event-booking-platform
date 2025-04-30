package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.SeatResponse;
import com.example.event_booking_platform.entity.Seat;
import com.example.event_booking_platform.entity.Show;
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

    public List<SeatResponse> getSeats(Long showId) throws ShowNotFoundException {
        Show show = showRepository.findById(showId).orElseThrow(() -> new ShowNotFoundException("Show not found"));
        List<Seat> seats = seatRepository.findAllByShow(show);
        return seats.stream().map((seat) -> SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build())
                .toList();
    }
}
