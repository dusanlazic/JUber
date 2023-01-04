package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ChatMessageResponse {
    private String content;
    private Date sentAt;
    private Boolean isFromSupport;
}
