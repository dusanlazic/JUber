package com.nwt.juber.service;

import com.nwt.juber.dto.request.LocalRegistrationRequest;
import com.nwt.juber.dto.request.OAuthRegistrationRequest;
import com.nwt.juber.dto.request.PasswordResetLinkRequest;
import com.nwt.juber.dto.request.PasswordResetRequest;
import com.nwt.juber.exception.EmailAlreadyInUseException;
import com.nwt.juber.exception.PhoneNumberAlreadyInUseException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.AuthProvider;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Role;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.PersonRepository;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.security.TokenProvider;
import com.nwt.juber.security.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public void registerWithOAuth(OAuthRegistrationRequest registrationRequest, Authentication authentication) {
        checkPhoneNumberAvailability(registrationRequest.getPhoneNumber());

        Passenger passenger = (Passenger) authentication.getPrincipal();
        passenger.setRole(Role.ROLE_PASSENGER);
        passenger.setName(registrationRequest.getFirstName(), registrationRequest.getLastName());
        passenger.setFirstName(registrationRequest.getFirstName());
        passenger.setLastName(registrationRequest.getLastName());
        passenger.setCity(registrationRequest.getCity());
        passenger.setPhoneNumber(registrationRequest.getPhoneNumber());
        userRepository.save(passenger);
    }

    public void verifyEmail(String token) {
        tokenProvider.validateToken(token, TokenType.VERIFICATION);
        UUID userId = tokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public void requestPasswordReset(PasswordResetLinkRequest resetLinkRequest) {
        Optional<User> possibleUser = userRepository.findByEmail(resetLinkRequest.getEmail());

        if (possibleUser.isEmpty()) {
            System.out.println("[!] User does not exist.");
            return;
        }

        User user = possibleUser.get();
        if (!user.getProvider().equals(AuthProvider.local)) {
            System.out.println("[!] User has no password.");
        } else if (!user.getEmailVerified()) {
            System.out.println("[!] User's email is not verified.");
        } else {
            // TODO: Send mail
            System.out.println("[+] Password reset token (30 min):");
            System.out.println(tokenProvider.createRecoveryToken(user));
        }
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        String token = passwordResetRequest.getToken();
        tokenProvider.validateToken(token, TokenType.RECOVERY);
        UUID userId = tokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        userRepository.save(user);
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyInUseException();
    }

    private void checkPhoneNumberAvailability(String phoneNumber) {
        if (personRepository.existsByPhoneNumber(phoneNumber))
            throw new PhoneNumberAlreadyInUseException();
    }
}
