package com.nwt.juber.service;

import com.nwt.juber.dto.request.LocalRegistrationRequest;
import com.nwt.juber.exception.EmailAlreadyInUseException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.AuthProvider;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Role;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.PersonRepository;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUserLocal(LocalRegistrationRequest registrationRequest) {
        checkEmailAvailability(registrationRequest.getEmail());
        checkPhoneNumberAvailability(registrationRequest.getPhoneNumber());

        Passenger passenger = new Passenger();
        passenger.setId(UUID.randomUUID());
        passenger.setRole(Role.ROLE_PASSENGER);
        passenger.setProvider(AuthProvider.local);
        passenger.setEmail(registrationRequest.getEmail());
        passenger.setEmailVerified(false);
        passenger.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        passenger.setName(registrationRequest.getFirstName(), registrationRequest.getLastName());
        passenger.setFirstName(registrationRequest.getFirstName());
        passenger.setLastName(registrationRequest.getLastName());
        passenger.setCity(registrationRequest.getCity());
        passenger.setPhoneNumber(registrationRequest.getPhoneNumber());
        userRepository.save(passenger);

        // TODO: Send mail
        System.out.println("[i] Make a POST request to this link to verify email:" );
        System.out.println("[i] http://localhost:8080/auth/register/verify/" + tokenProvider.createEmailVerificationToken(passenger));
    }

    public void verifyEmail(String token) {
        if (StringUtils.hasLength(token) && tokenProvider.validateEmailVerificationToken(token)) {
            UUID userId = tokenProvider.getUserIdFromToken(token);

            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            user.setEmailVerified(true);
            userRepository.save(user);
        }
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyInUseException("Email address is used by another user.");
    }

    private void checkPhoneNumberAvailability(String phoneNumber) {
        if (personRepository.existsByPhoneNumber(phoneNumber))
            throw new EmailAlreadyInUseException("Phone number is used by another user.");
    }
}
