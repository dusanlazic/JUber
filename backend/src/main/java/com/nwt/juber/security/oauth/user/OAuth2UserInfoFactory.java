package com.nwt.juber.security.oauth.user;

import com.nwt.juber.exception.NotImplementedException;
import com.nwt.juber.model.AuthProvider;
import com.nwt.juber.model.AuthProvider.*;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (AuthProvider.valueOf(registrationId)) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case FACEBOOK -> throw new NotImplementedException("Facebook login is not implemented yet.");
            default -> throw new IllegalArgumentException("Unknown provider type.");
        };
    }

}
