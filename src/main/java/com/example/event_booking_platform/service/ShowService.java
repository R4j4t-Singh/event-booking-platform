package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.CreateShowRequest;
import com.example.event_booking_platform.entity.Event;
import com.example.event_booking_platform.entity.Seat;
import com.example.event_booking_platform.entity.SeatStatus;
import com.example.event_booking_platform.entity.Show;
import com.example.event_booking_platform.exception.EventNotFoundException;
import com.example.event_booking_platform.repository.EventRepository;
import com.example.event_booking_platform.repository.SeatRepository;
import com.example.event_booking_platform.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional(rollbackFor = Exception.class)
    public Show addShow(CreateShowRequest request) throws EventNotFoundException {
        Event event = eventRepository.findById(request.getEventId()).orElseThrow(() -> new EventNotFoundException("Event does not exist with this id"));

        Show show = Show.builder()
                .event(event)
                .venue(request.getVenue())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        Show savedShow =  showRepository.save(show);

        List<Seat> seats = generateSeats(savedShow, request.getTotalSeats());
        seats.forEach((seat) -> seatRepository.save(seat));

        return savedShow;
    }

    public List<Seat> generateSeats(Show savedShow, Integer totalSeats ) {
        List<Seat> seats = new ArrayList<>();
        for(int i=0;i<totalSeats;i++) {
            Seat seat = Seat.builder()
                    .seatNumber("S" + i)
                    .show(savedShow)
                    .status(SeatStatus.AVAILABLE)
                    .build();
            seats.add(seat);
        }

        return seats;
    }

    public List<Show> getShows(Long eventId) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event does not exist with this id"));
        return showRepository.findAllByEvent(event);
    }

    public void removeShow(Long id) {
        showRepository.deleteById(id);
    }
}
