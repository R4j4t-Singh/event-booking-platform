package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.BookingRequest;
import com.example.event_booking_platform.entity.*;
import com.example.event_booking_platform.exception.SeatUnavailableException;
import com.example.event_booking_platform.exception.ShowNotFoundException;
import com.example.event_booking_platform.repository.BookingRepository;
import com.example.event_booking_platform.repository.SeatRepository;
import com.example.event_booking_platform.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public Booking bookSeats(BookingRequest request) throws ShowNotFoundException, SeatUnavailableException {

        Show show = showRepository.findById(request.getShowId()).orElseThrow(() -> new ShowNotFoundException("Show not found"));

        List<Seat> selectedSeats = seatRepository.findAllById(request.getSeats());

        List<Seat> unavailableSeats = selectedSeats.stream().filter((seat) -> seat.getStatus() != SeatStatus.AVAILABLE).toList();

        if(!unavailableSeats.isEmpty()) {
            throw new SeatUnavailableException("Some seats are unavailable");
        }

        selectedSeats.forEach((seat) -> seat.setStatus(SeatStatus.BOOKED));
        seatRepository.saveAll(selectedSeats);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        Booking booking = Booking.builder()
                .show(show)
                .seats(selectedSeats)
                .bookingTime(new Date(System.currentTimeMillis()))
                .userId(userId)
                .status("CONFIRMED")
                .build();

        return bookingRepository.save(booking);
    }
}
