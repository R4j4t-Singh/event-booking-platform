package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.BookingEmailData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "email", groupId = "consumer-email-group")
    public void bookingEmailConsumer(BookingEmailData emailData) {
        log.debug("Kafka : Consuming -> {}", emailData);
        sendBookingEmail(emailData);
    }

    public void sendBookingEmail(BookingEmailData emailData) {
        if(emailData != null) {
            String seats = emailData.getSeatNumbers().stream().reduce((a, b) -> a + ", " + b).orElse("");

            String emailBody = "Thanks for booking with us" + "\n" +
                    "Here are your booking details" + "\n" +
                    "Event: " + emailData.getEvent() + "\n" +
                    "Show Time: " + emailData.getStartTime().toString() + " - " + emailData.getEndTime().toString() + "\n" +
                    "Venue: " + emailData.getVenue() + "\n" +
                    "Seats: " + seats;

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailData.getEmail());
            mailMessage.setSubject("Your booking for the event " + emailData.getEvent() + " is confirmed");
            mailMessage.setText(emailBody);

            javaMailSender.send(mailMessage);
        } else {
            log.error("Email Data is null");
        }
    }
}
