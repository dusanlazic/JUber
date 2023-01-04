package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatConversationResponse {
    private String messagePreview;
    private Date date;
    private UUID userId;
    private String userFullName;
    private String userImageUrl;
}
