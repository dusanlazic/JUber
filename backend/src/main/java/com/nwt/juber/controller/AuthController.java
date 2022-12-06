package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.*;
import com.nwt.juber.dto.response.OAuth2UserInfoResponse;
import com.nwt.juber.dto.response.TokenResponse;
import com.nwt.juber.model.User;
import com.nwt.juber.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return accountService.login(loginRequest, response);
    }

    @PostMapping("/logout")
    public ResponseOk logout(HttpServletRequest request, HttpServletResponse response) {
        accountService.logout(request, response);
        return new ResponseOk("Success");
    }

    @PostMapping("/register")
    public ResponseOk register(@Valid @RequestBody LocalRegistrationRequest registrationRequest) {
        accountService.registerUserLocal(registrationRequest);
        return new ResponseOk("User registered successfully.");
    }

    @PatchMapping("/register/oauth")
    @PreAuthorize("hasRole('PASSENGER_NEW')")
    public ResponseOk registerWithOAuth(@Valid @RequestBody OAuthRegistrationRequest registrationRequest, Authentication authentication) {
        accountService.registerWithOAuth(registrationRequest, authentication);
        return new ResponseOk("User registered successfully.");
    }

    @PostMapping("/register/verify/{token}")
    public ResponseOk verifyEmail(@PathVariable String token) {
        accountService.verifyEmail(token);
        return new ResponseOk("Email verified successfully.");
    }

    @PostMapping("/recovery")
    public ResponseOk requestPasswordReset(@Valid @RequestBody PasswordResetLinkRequest resetLinkRequest) {
        accountService.requestPasswordReset(resetLinkRequest);
        return new ResponseOk("Password reset requested.");
    }

    @PatchMapping("/recovery")
    public ResponseOk resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        accountService.resetPassword(passwordResetRequest);
        return new ResponseOk("Password successfully changed.");
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('PASSENGER_NEW', 'PASSENGER', 'DRIVER', 'ADMIN')")
    public OAuth2UserInfoResponse me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new OAuth2UserInfoResponse(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), user.getAuthorities().stream().findFirst().get().toString());
    }

}
