package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

import com.nwt.juber.model.AuthProvider;

@Data
@AllArgsConstructor
public class OAuth2UserInfoResponse {
    private UUID id;
    private String name;
    private String email;
    private String imageUrl;
    private String role;
    private AuthProvider provider;
}
