package com.nwt.juber.security;

import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public UserDetails loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
