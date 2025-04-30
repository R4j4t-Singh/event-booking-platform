package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.CreateEventRequest;
import com.example.event_booking_platform.dto.EventResponse;
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

    public List<EventResponse> getEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::getEventResponse).toList();
    }

    public EventResponse getEvent(Long id) throws EventNotFoundException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found with this id"));
        return getEventResponse(event);
    }

    public EventResponse addEvent(CreateEventRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        log.debug("userID: {}", userId);

        Event event = Event.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .createdBy(userId)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

        log.debug("created at: {}", event.getCreatedAt());
        Event savedEvent = eventRepository.save(event);
        return getEventResponse(savedEvent);
    }

    public void removeEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public EventResponse getEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .category(event.getCategory())
                .imageUrl(event.getImageUrl())
                .description(event.getDescription())
                .createdBy(event.getCreatedBy())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
