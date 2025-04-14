package com.example.event_booking_platform.repository;

import com.example.event_booking_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
