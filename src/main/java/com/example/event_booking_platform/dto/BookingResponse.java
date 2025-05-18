package com.example.event_booking_platform.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String event;
    private String venue;
    private Date startTime;
    private Date endTime;
    private Date bookingTime;
    private List<String> seats;
    private String status;
}
