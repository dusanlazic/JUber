package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BlockedUserResponse {
    private UUID userId;
    private String fullName;
    private String role;
    private String note;
}
