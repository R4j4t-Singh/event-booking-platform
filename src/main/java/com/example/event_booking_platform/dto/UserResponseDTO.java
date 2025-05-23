package com.example.event_booking_platform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String name;
    private String email;
    private String role;
}
