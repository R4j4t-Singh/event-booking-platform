package com.example.event_booking_platform.service;

import com.example.event_booking_platform.entity.Show;
import com.example.event_booking_platform.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    public Show addShow(Show show, Long eventId) {
        show.setEventId(eventId);
        return showRepository.save(show);
    }

    public List<Show> getShows(Long eventId) {
        return showRepository.findByEventId(eventId);
    }

    public void removeShow(Long id) {
        showRepository.deleteById(id);
    }
}
