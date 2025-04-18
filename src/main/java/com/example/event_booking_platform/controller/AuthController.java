package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.entity.User;
import com.example.event_booking_platform.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> Register(@RequestBody User user) {
        User savedUser = authService.register(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        String token = authService.login(user);
        Cookie cookie = new Cookie("sessionToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api");
        response.addCookie(cookie);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
