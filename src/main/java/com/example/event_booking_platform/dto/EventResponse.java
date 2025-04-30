package com.example.event_booking_platform.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private Long createdBy;
    private Date createdAt;
}
