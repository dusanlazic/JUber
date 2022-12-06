package com.nwt.juber.security.oauth;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.exception.BadRequestException;
import com.nwt.juber.security.TokenAuthenticationFilter;
import com.nwt.juber.security.TokenProvider;
import com.nwt.juber.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.nwt.juber.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    @Qualifier("httpCookieOAuth2AuthorizationRequestRepository")
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        String accessToken = tokenProvider.createAccessToken(auth);
        Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
        String targetUrl = determineTargetUrl(request, response, expiresAt);

        if (response.isCommitted())
            return;

        Integer tokenExpirationSeconds = appProperties.getAuth().getTokenExpirationSeconds();
        CookieUtils.addCookie(
                response,
                TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                tokenExpirationSeconds
        );

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Long expiresAt) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get()))
            throw new BadRequestException();

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder
                .fromUriString(targetUrl)
                .queryParam("expiresAt", expiresAt)
                .build()
                .toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieRepository.removeAuthorizationRequestCookies(request, response);
    }

    private Boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(u -> {
                    URI authorizedURI = URI.create(u);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}

