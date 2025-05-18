package com.example.event_booking_platform.controller;

import com.example.event_booking_platform.dto.LoginRequest;
import com.example.event_booking_platform.dto.LoginResponse;
import com.example.event_booking_platform.dto.UserResponseDTO;
import com.example.event_booking_platform.entity.User;
import com.example.event_booking_platform.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        Cookie cookie = new Cookie("sessionToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        LoginResponse loginResponse = LoginResponse.builder().token(token).build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getCurrentUser(HttpServletRequest request) {
        UserResponseDTO user = authService.getCurrentUser(request);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
