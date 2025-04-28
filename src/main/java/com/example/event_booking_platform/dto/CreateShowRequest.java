package com.example.event_booking_platform.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CreateShowRequest {
    private Long eventId;
    private String venue;
    private Date startTime;
    private Date endTime;
    private Integer totalSeats;
}
