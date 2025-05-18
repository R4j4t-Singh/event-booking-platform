package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.BookingRequest;
import com.example.event_booking_platform.dto.BookingResponse;
import com.example.event_booking_platform.entity.*;
import com.example.event_booking_platform.exception.SeatUnavailableException;
import com.example.event_booking_platform.exception.SeatsLockedException;
import com.example.event_booking_platform.exception.ShowNotFoundException;
import com.example.event_booking_platform.dto.BookingEmailData;
import com.example.event_booking_platform.repository.BookingRepository;
import com.example.event_booking_platform.repository.SeatRepository;
import com.example.event_booking_platform.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private LockService lockService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, BookingEmailData> kafkaTemplate;

    @Transactional
    public BookingResponse bookSeats(BookingRequest request) throws ShowNotFoundException, SeatUnavailableException, SeatsLockedException {

        String lockOwner = UUID.randomUUID().toString();
        List<String> lockedSeats = new ArrayList<>();

        try {
            for(Long seatId : request.getSeats()) {
                String lockKey = "seat_lock:" + seatId;
                boolean lock = lockService.acquireLock(lockKey, lockOwner, 300);

                if(!lock) throw new SeatsLockedException("Seats are locked");

                lockedSeats.add(lockKey);
            };

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
            String userEmail = userPrincipal.getUsername();

            Booking booking = Booking.builder()
                    .show(show)
                    .seats(selectedSeats)
                    .bookingTime(new Date(System.currentTimeMillis()))
                    .userId(userId)
                    .status("CONFIRMED")
                    .build();

            Booking savedBooking = bookingRepository.save(booking);
            sendBookingEmail(booking, userEmail);
            return getBookingResponse(savedBooking);
        } finally {
            lockedSeats.forEach((lockKey) -> {
                lockService.releaseLock(lockKey, lockOwner);
            });
        }
    }

    public List<BookingResponse> getBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        return bookings.stream().map((this::getBookingResponse)).toList();
    }

    public BookingResponse getBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .venue(booking.getShow().getVenue())
                .event(booking.getShow().getEvent().getTitle())
                .startTime(booking.getShow().getStartTime())
                .endTime(booking.getShow().getEndTime())
                .bookingTime(booking.getBookingTime())
                .seats(booking.getSeats().stream().map(Seat::getSeatNumber).toList())
                .status(booking.getStatus())
                .build();
    }

    public void sendBookingEmail(Booking booking, String email) {
        BookingEmailData emailData = BookingEmailData.builder()
                .email(email)
                .seatNumbers(booking.getSeats().stream().map(Seat::getSeatNumber).toList())
                .venue(booking.getShow().getVenue())
                .event(booking.getShow().getEvent().getTitle())
                .startTime(booking.getShow().getStartTime())
                .endTime(booking.getShow().getEndTime())
                .build();

//        emailService.sendBookingEmail(emailData);
        kafkaTemplate.send("email", emailData);
    }
}
