package com.nwt.juber.service;

import com.nwt.juber.exception.MethodNotImplementedException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails user) {
        User newUser = (User) user;
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new MethodNotImplementedException();
    }

    @Override
    public void deleteUser(String username) {
        throw new MethodNotImplementedException();
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
