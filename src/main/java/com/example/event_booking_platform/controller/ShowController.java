package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.entity.Show;
import com.example.event_booking_platform.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/events")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/{eventId}/shows")
    public ResponseEntity<List<Show>> getShows(@PathVariable Long eventId) {
        List<Show> shows = showService.getShows(eventId);
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }
}
