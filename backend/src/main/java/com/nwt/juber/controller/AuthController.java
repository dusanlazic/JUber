package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.LocalRegistrationRequest;
import com.nwt.juber.dto.request.LoginRequest;
import com.nwt.juber.dto.response.OAuth2UserInfoResponse;
import com.nwt.juber.dto.response.TokenResponse;
import com.nwt.juber.model.User;
import com.nwt.juber.security.TokenProvider;
import com.nwt.juber.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        return new TokenResponse(tokenProvider.createAccessToken(authentication));
    }

    @PostMapping("/register")
    public ResponseOk register(@Valid @RequestBody LocalRegistrationRequest registrationRequest) {
        accountService.registerUserLocal(registrationRequest);
        return new ResponseOk("User registered successfully.");
    }

    @PostMapping("/register/verify/{token}")
    public ResponseOk verifyEmail(@PathVariable String token) {
        accountService.verifyEmail(token);
        return new ResponseOk("Email verified successfully.");
    }

    @GetMapping("/me")
    public OAuth2UserInfoResponse me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new OAuth2UserInfoResponse(user.getId(), user.getName(), user.getEmail(), user.getImageUrl());
    }

}
