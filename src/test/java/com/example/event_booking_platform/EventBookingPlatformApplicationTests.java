package com.example.event_booking_platform;

import com.example.event_booking_platform.dto.BookingEmailData;
import com.example.event_booking_platform.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class EventBookingPlatformApplicationTests {

	@Autowired
	private EmailService emailService;

	@Test
	void sendBookingEmail() {
		BookingEmailData testData = BookingEmailData.builder()
				.email("test")
				.event("Big Bash")
				.venue("Lane 01, Panchwati Colony, Phase 01, Baniyawala")
				.seatNumbers(List.of("A10", "A23", "A50"))
				.startTime(new Date(System.currentTimeMillis()))
				.endTime(new Date(System.currentTimeMillis()))
				.build();
		emailService.sendBookingEmail(testData);
	}

}
