package com.nwt.juber.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.dto.request.DriverRegistrationRequest;
import com.nwt.juber.dto.request.LocalRegistrationRequest;
import com.nwt.juber.dto.request.LoginRequest;
import com.nwt.juber.dto.request.OAuthRegistrationRequest;
import com.nwt.juber.dto.request.PasswordResetLinkRequest;
import com.nwt.juber.dto.request.PasswordResetRequest;
import com.nwt.juber.dto.response.TokenResponse;
import com.nwt.juber.exception.EmailAlreadyInUseException;
import com.nwt.juber.exception.InvalidPasswordRequestException;
import com.nwt.juber.exception.InvalidRecoveryTokenException;
import com.nwt.juber.exception.PhoneNumberAlreadyInUseException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.AuthProvider;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverShift;
import com.nwt.juber.model.DriverStatus;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Role;
import com.nwt.juber.model.User;
import com.nwt.juber.model.Vehicle;
import com.nwt.juber.repository.PersonRepository;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.security.TokenAuthenticationFilter;
import com.nwt.juber.security.TokenProvider;
import com.nwt.juber.security.TokenType;
import com.nwt.juber.util.CookieUtils;

@Service
public class AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppProperties appProperties;

    public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createAccessToken(authentication);
        Integer tokenExpirationSeconds = appProperties.getAuth().getTokenExpirationSeconds();
        CookieUtils.addCookie(
                response,
                TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                tokenExpirationSeconds
        );

        Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
        return new TokenResponse(accessToken, expiresAt);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME);
    }

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
        /*
        Complete registration for the passengers that sign in with OAuth for the first time.
         */
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

    public void registerDriver(DriverRegistrationRequest registrationRequest) {
        checkEmailAvailability(registrationRequest.getEmail());
        checkPhoneNumberAvailability(registrationRequest.getPhoneNumber());

        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setBabyFriendly(registrationRequest.getBabyFriendly());
        vehicle.setPetFriendly(registrationRequest.getPetFriendly());
        vehicle.setCapacity(registrationRequest.getCapacity());

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setRole(Role.ROLE_DRIVER);
        driver.setProvider(AuthProvider.local);
        driver.setEmail(registrationRequest.getEmail());
        driver.setEmailVerified(true);
        driver.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        driver.setName(registrationRequest.getFirstName(), registrationRequest.getLastName());
        driver.setFirstName(registrationRequest.getFirstName());
        driver.setLastName(registrationRequest.getLastName());
        driver.setCity(registrationRequest.getCity());
        driver.setPhoneNumber(registrationRequest.getPhoneNumber());
        driver.setStatus(DriverStatus.INACTIVE);
        driver.setDriverShifts(new ArrayList<DriverShift>());
        driver.setVehicle(vehicle);

        userRepository.save(driver);
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
        	throw new UserNotFoundException("User does not exist.");
        }

        User user = possibleUser.get();
        if (!user.getProvider().equals(AuthProvider.local)) {
        	throw new InvalidPasswordRequestException("User has no password.");
        } else if (!user.getEmailVerified()) {
        	throw new InvalidPasswordRequestException("User's email is not verified.");
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
        if (user.getModified() != null && tokenProvider.readClaims(token).getIssuedAt().before(user.getModified()))
            throw new InvalidRecoveryTokenException("Password reset link is invalidated.");
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
