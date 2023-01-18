package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProfileChangeRequestResponse {
    private UUID requestId;
    private UUID userId;
    private String userFullName;
    private Map<String, String> changes;
    private Date requestedAt;
}
