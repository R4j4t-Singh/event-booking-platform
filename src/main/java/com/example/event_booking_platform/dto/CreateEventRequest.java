package com.example.event_booking_platform.dto;

import lombok.Data;

@Data
public class CreateEventRequest {
    private String title;
    private String description;
    private String category;
    private String imageUrl;
}
