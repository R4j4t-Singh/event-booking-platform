package com.example.event_booking_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EventBookingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBookingPlatformApplication.class, args);
	}

}
