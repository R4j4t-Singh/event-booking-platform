package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.dto.EventResponse;
import com.example.event_booking_platform.dto.ShowResponse;
import com.example.event_booking_platform.service.EventService;
import com.example.event_booking_platform.service.ShowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
public class PublicController {
    @Autowired
    private EventService eventService;

    @Autowired
    private ShowService showService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents() {
        List<EventResponse> events = eventService.getEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id) {
        try {
            EventResponse event = eventService.getEvent(id);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{eventId}/shows")
    public ResponseEntity<List<ShowResponse>> getShows(@PathVariable Long eventId) {
        try {
            List<ShowResponse> shows = showService.getShows(eventId);
            return new ResponseEntity<>(shows, HttpStatus.OK);
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
