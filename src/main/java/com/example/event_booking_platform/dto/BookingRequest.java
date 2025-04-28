package com.example.event_booking_platform.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    private Long showId;
    private List<Long> seats;
}
