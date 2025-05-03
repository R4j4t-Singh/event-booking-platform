package com.example.event_booking_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEmailData {
    private String email;
    private List<String> seatNumbers;
    private String event;
    private Date startTime;
    private Date endTime;
    private String venue;
}
