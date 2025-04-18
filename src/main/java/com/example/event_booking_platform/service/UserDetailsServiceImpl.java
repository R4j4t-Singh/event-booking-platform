package com.example.event_booking_platform.service;

import com.example.event_booking_platform.entity.User;
import com.example.event_booking_platform.entity.UserPrincipal;
import com.example.event_booking_platform.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if(user == null) throw new UsernameNotFoundException("User not found with this email");
        log.debug(user.getName());
        return new UserPrincipal(user);
    }
}
