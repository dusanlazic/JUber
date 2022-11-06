package com.nwt.juber.security.oauth;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.exception.BadRequestException;
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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, auth);

        if (response.isCommitted())
            return;

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get()))
            throw new BadRequestException();

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String token = tokenProvider.createAccessToken(auth);

        return UriComponentsBuilder
                .fromUriString(targetUrl)
                .queryParam("token", token)
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

