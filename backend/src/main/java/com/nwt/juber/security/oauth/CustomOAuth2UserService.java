package com.nwt.juber.security.oauth;

import com.nwt.juber.exception.OAuth2AuthenticationProcessingException;
import com.nwt.juber.model.AuthProvider;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Role;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.security.oauth.user.OAuth2UserInfo;
import com.nwt.juber.security.oauth.user.OAuth2UserInfoFactory;
import com.nwt.juber.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail()))
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider.");

        Optional<User> possibleUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user;
        if (possibleUser.isPresent()) {
            user = possibleUser.get();
            AuthProvider provider = user.getProvider();

            if (provider.equals(AuthProvider.local))
                user = assignProvider(user, oAuth2UserRequest, oAuth2UserInfo);
            else if (!provider.equals(getAuthProvider(oAuth2UserRequest)))
                throw new OAuth2AuthenticationProcessingException("User is signed up with " + user.getProvider() + " account.");
        } else {
            user = registerUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        user.setAttributes(oAuth2User.getAttributes());

        return user;
    }

    private User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        /*
        Passengers are the only type of users allowed to register by themselves. For that reason, we can assume
        that the new user's role will be passenger.
         */
        Passenger passenger = new Passenger();

        passenger.setId(UUID.randomUUID());
        passenger.setProvider(getAuthProvider(oAuth2UserRequest));
        passenger.setProviderId(oAuth2UserInfo.getId());
        passenger.setName(oAuth2UserInfo.getName());
        passenger.setEmail(oAuth2UserInfo.getEmail());
        passenger.setImageUrl(fileStorageService.store(oAuth2UserInfo.getImageUrl(), MediaType.IMAGE_JPEG_VALUE));
        passenger.setEmailVerified(true);
        passenger.setRole(Role.ROLE_PASSENGER_NEW);
        return userRepository.save(passenger);
    }

    private User assignProvider(User user, OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        /*
        Allow local registered users to link their Google/Facebook accounts.
         */
        user.setProvider(getAuthProvider(oAuth2UserRequest));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private AuthProvider getAuthProvider(OAuth2UserRequest oAuth2UserRequest) {
        return AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId());
    }

}
