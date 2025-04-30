package com.example.event_booking_platform.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long showId;
    private Date bookingTime;
    private List<Long> seats;
    private String status;
}
