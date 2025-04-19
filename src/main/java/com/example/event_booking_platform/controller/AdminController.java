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

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ShowService showService;

    @PostMapping("/events")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        Event savedEvent = eventService.addEvent(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> removeEvent(@PathVariable Long id) {
        eventService.removeEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO PUT


    @PostMapping("/events/{eventId}/shows")
    public ResponseEntity<Show> addShow(@PathVariable Long eventId, @RequestBody Show show) {
        try {
            Show savedShow = showService.addShow(show, eventId);
            return new ResponseEntity<>(savedShow, HttpStatus.CREATED);
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/shows/{id}")
    public ResponseEntity<?> removeShow(@PathVariable Long id) {
        showService.removeShow(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO PUT
}
