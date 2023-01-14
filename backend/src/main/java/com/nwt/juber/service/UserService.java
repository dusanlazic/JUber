package com.nwt.juber.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.UserRepository;

@Service
public class UserService {

	 @Autowired
	 private UserRepository userRepository;
	 
	 public User fetchUserWithNotificationsById(UUID id) {
		 return userRepository.getUserWithNotificationsById(id).orElseThrow(UserNotFoundException::new);
	 }
}
