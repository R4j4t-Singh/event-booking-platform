package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.entity.Event;
import com.example.event_booking_platform.entity.Show;
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
    public ResponseEntity<List<Event>> getEvents() {
        List<Event> events = eventService.getEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        try {
            Event event = eventService.getEvent(id);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{eventId}/shows")
    public ResponseEntity<List<Show>> getShows(@PathVariable Long eventId) {
        try {
            List<Show> shows = showService.getShows(eventId);
            return new ResponseEntity<>(shows, HttpStatus.OK);
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
