package com.example.event_booking_platform.service;

import com.example.event_booking_platform.dto.LoginRequest;
import com.example.event_booking_platform.dto.UserResponseDTO;
import com.example.event_booking_platform.entity.User;
import com.example.event_booking_platform.repository.UserRepository;
import com.example.event_booking_platform.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtUtil jwtUtil;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        log.debug(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return jwtUtil.generateToken(request.getEmail());
    }

    public UserResponseDTO getCurrentUser(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("sessionToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if(token == null) return null;

        String email = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(email);

        if(user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
