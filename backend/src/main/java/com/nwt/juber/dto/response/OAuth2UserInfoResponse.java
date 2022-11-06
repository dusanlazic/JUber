package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OAuth2UserInfoResponse {
    private UUID id;
    private String name;
    private String email;
    private String imageUrl;
    private String role;
}
