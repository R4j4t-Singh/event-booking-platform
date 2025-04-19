package com.example.event_booking_platform.service;

import com.example.event_booking_platform.entity.Event;
import com.example.event_booking_platform.entity.UserPrincipal;
import com.example.event_booking_platform.exception.EventNotFoundException;
import com.example.event_booking_platform.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(Long id) throws EventNotFoundException {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found with this id"));
    }

    public Event addEvent(Event event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        log.debug("userID: {}", userId);

        event.setCreatedBy(userId);
        event.setCreatedAt(new Date(System.currentTimeMillis()));
        log.debug("created at: {}", event.getCreatedAt());
        return eventRepository.save(event);
    }

    public void removeEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
