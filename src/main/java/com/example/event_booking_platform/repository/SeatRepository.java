package com.example.event_booking_platform.repository;

import com.example.event_booking_platform.entity.Seat;
import com.example.event_booking_platform.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByShow(Show show);
}
