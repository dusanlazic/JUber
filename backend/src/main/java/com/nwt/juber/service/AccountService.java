package com.nwt.juber.service;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.config.AppProperties;
import com.nwt.juber.dto.request.*;
import com.nwt.juber.dto.response.*;
import com.nwt.juber.exception.*;
import com.nwt.juber.model.*;
import com.nwt.juber.repository.PersonRepository;
import com.nwt.juber.repository.ProfileChangeRequestRepository;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.security.TokenAuthenticationFilter;
import com.nwt.juber.security.TokenProvider;
import com.nwt.juber.security.TokenType;
import com.nwt.juber.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProfileChangeRequestRepository profileChangeRequestRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FileStorageService storageService;

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
        driver.setActive(false);
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

    public PhotoUploadResponse setProfilePicture(MultipartFile file, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String imageUrl = storageService.store(file);

        user.setImageUrl(imageUrl);
        userRepository.save(user);
        return new PhotoUploadResponse(imageUrl);
    }

    public void removeProfilePicture(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setImageUrl(null);
        userRepository.save(user);
    }

    public ProfileInfoResponse getProfileInfo(Authentication authentication) {
        Person person = (Person) authentication.getPrincipal();
        return new ProfileInfoResponse(
                person.getFirstName(),
                person.getLastName(),
                person.getCity(),
                person.getPhoneNumber(),
                person.getImageUrl()
        );
    }

    public ResponseOk updateProfileInfo(ProfileInfoChangeRequest profileInfo, Authentication authentication) {
        Person person = (Person) authentication.getPrincipal();

        Map<String, String> changes = extractChangesFromRequestBody(profileInfo);
        ProfileChangeRequest changeRequest = new ProfileChangeRequest(person, changes);

        switch (person.getRole()) {
            case ROLE_PASSENGER -> {
                applyProfileChanges(changeRequest);
                personRepository.save(person);
                return new ResponseOk("Changes saved.");
            }
            case ROLE_DRIVER -> {
                profileChangeRequestRepository.save(changeRequest);
                return new ResponseOk("Changes requested.");
            }
            default -> {
                return null;
            }
        }
    }

    public List<ProfileChangeRequestResponse> getPendingProfileChangeRequests() {
        return profileChangeRequestRepository.findPending()
                .stream().map(r -> new ProfileChangeRequestResponse(
                        r.getId(),
                        r.getPerson().getId(),
                        r.getPerson().getName(),
                        r.getChanges(),
                        r.getRequestedAt()))
                .toList();
    }

    public ResponseOk resolveProfileChangeRequest(UUID requestId, ProfileInfoChangeResolveRequest resolveRequest) {
        ProfileChangeRequest changeRequest = profileChangeRequestRepository.findById(requestId).orElseThrow(ProfileChangeRequestNotFoundException::new);

        if (!changeRequest.getStatus().equals(ChangeRequestStatus.PENDING))
            throw new ProfileChangeRequestAlreadyResolvedException();

        ChangeRequestStatus newStatus = ChangeRequestStatus.valueOf(resolveRequest.getNewStatus());
        switch (newStatus) {
            case APPROVED -> {
                applyProfileChanges(changeRequest);
                changeRequest.setStatus(ChangeRequestStatus.APPROVED);

                profileChangeRequestRepository.save(changeRequest);
                personRepository.save(changeRequest.getPerson());
                return new ResponseOk("Changes saved.");
            }
            case DENIED -> {
                changeRequest.setStatus(ChangeRequestStatus.DENIED);
                profileChangeRequestRepository.save(changeRequest);
                return new ResponseOk("Changes denied.");
            }
            default -> {
                return null;
            }
        }
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyInUseException();
    }

    private void checkPhoneNumberAvailability(String phoneNumber) {
        if (personRepository.existsByPhoneNumber(phoneNumber))
            throw new PhoneNumberAlreadyInUseException();
    }

    private Map<String, String> extractChangesFromRequestBody(ProfileInfoChangeRequest changeRequestData) {
        Map<String, String> changes = new HashMap<>();

        changes.put("firstName", changeRequestData.getFirstName());
        changes.put("lastName", changeRequestData.getLastName());
        changes.put("city", changeRequestData.getCity());
        changes.put("phoneNumber", changeRequestData.getPhoneNumber());
        changes.values().removeAll(Collections.singleton(null));

        return changes;
    }

    private void applyProfileChanges(ProfileChangeRequest changeRequest) {
        Map<String, String> changes = changeRequest.getChanges();
        Person person = changeRequest.getPerson();

        person.setFirstName(changes.getOrDefault("firstName", person.getFirstName()));
        person.setLastName(changes.getOrDefault("lastName", person.getLastName()));
        person.setCity(changes.getOrDefault("city", person.getCity()));
        person.setPhoneNumber(changes.getOrDefault("phoneNumber", person.getPhoneNumber()));
    }
}
