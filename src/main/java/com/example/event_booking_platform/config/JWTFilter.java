package com.example.event_booking_platform.config;

import com.example.event_booking_platform.service.UserDetailsServiceImpl;
import com.example.event_booking_platform.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        String authorization = request.getHeader("Authorization");
//        log.debug("authorization {}", authorization);
//
//        String token = null;
//        if(authorization != null && authorization.startsWith("Bearer ")) {
//            token = authorization.substring(7);
//        }

        Cookie[] cookies = request.getCookies();
        Cookie sessionCookie = null;

        if(cookies != null)
            sessionCookie = Arrays.stream(cookies).filter((cookie -> cookie.getName().equals("sessionToken"))).findFirst().orElse(null);

        String token = null;
        if(sessionCookie != null) {
            token = sessionCookie.getValue();
        }
        log.debug("token {}", token);

        if(token != null && jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String email = jwtUtil.extractUsername(token);
            log.debug("email {}", email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
