package com.example.event_booking_platform.dto;

import com.example.event_booking_platform.entity.SeatStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private SeatStatus status;
}
