package com.example.event_booking_platform.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ShowResponse {
    private Long id;
    private String venue;
    private Date startTime;
    private Date endTime;
}
