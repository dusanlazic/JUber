package com.nwt.juber.security.oauth.user;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        try {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
            return (String) dataObj.get("url");
        } catch (NullPointerException e) {
            return null;
        }
    }
}
