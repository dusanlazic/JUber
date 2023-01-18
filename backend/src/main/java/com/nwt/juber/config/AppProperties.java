package com.nwt.juber.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    private final Payment payment = new Payment();
    private final Uploads uploads = new Uploads();
    private final Mailing mailing = new Mailing();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private Integer tokenExpirationSeconds;
        private Integer verificationTokenExpirationMinutes;
        private Integer recoveryTokenExpirationMinutes;
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    @Getter
    @Setter
    public static class Payment {
        private String etherscanKey;
        private String etherscanUrl;
        private String cryptocompareKey;
        private String cryptocompareUrl;
        private Integer pendingTimeoutSeconds;
    }

    @Getter
    @Setter
    public static class Uploads {
        private String uploadsLocation;
        private String servingEndpoint;
        private Set<String> allowedContentTypes;
    }

    @Getter
    @Setter
    public static class Mailing {
        private String templatesLocation;
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

    public Payment getPayment() {
        return payment;
    }

    public Uploads getUploads() {
        return uploads;
    }

    public Mailing getMailing() { return mailing; }
}
