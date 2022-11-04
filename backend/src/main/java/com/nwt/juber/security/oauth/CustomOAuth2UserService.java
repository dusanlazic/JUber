package com.nwt.juber.security.oauth;

import com.nwt.juber.exception.OAuth2AuthenticationProcessingException;
import com.nwt.juber.model.AuthProvider;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.security.UserPrincipal;
import com.nwt.juber.security.oauth.user.OAuth2UserInfo;
import com.nwt.juber.security.oauth.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
            if (!user.getProvider().equals(getAuthProvider(oAuth2UserRequest)))
                throw new OAuth2AuthenticationProcessingException("User is signed up with " + user.getProvider() + " account.");
            user = updateUser(user, oAuth2UserInfo);
        } else {
            user = registerUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.fromUser(user, oAuth2User.getAttributes());
    }

    private User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setProvider(getAuthProvider(oAuth2UserRequest));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private AuthProvider getAuthProvider(OAuth2UserRequest oAuth2UserRequest) {
        return AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId());
    }

}
