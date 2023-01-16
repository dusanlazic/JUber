package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatConversationResponse {
    private String messagePreview;
    private Date date;
    private UUID userId;
    private String userFullName;
    private String userImageUrl;
    private Boolean isResponded;
}
