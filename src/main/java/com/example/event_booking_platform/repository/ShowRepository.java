package com.example.event_booking_platform.repository;

import com.example.event_booking_platform.entity.Event;
import com.example.event_booking_platform.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByEvent(Event event);
}
