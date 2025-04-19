package com.example.event_booking_platform.service;

import com.example.event_booking_platform.entity.Event;
import com.example.event_booking_platform.entity.Show;
import com.example.event_booking_platform.exception.EventNotFoundException;
import com.example.event_booking_platform.repository.EventRepository;
import com.example.event_booking_platform.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private EventRepository eventRepository;

    public Show addShow(Show show, Long eventId) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event does not exist with this id"));
        show.setEvent(event);
        return showRepository.save(show);
    }

    public List<Show> getShows(Long eventId) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event does not exist with this id"));
        return showRepository.findByEvent(event);
    }

    public void removeShow(Long id) {
        showRepository.deleteById(id);
    }
}
