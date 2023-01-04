package com.nwt.juber.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MsgFromUserMessage {
    private String content;
    private Date sentAt;
    private UUID userId;
}
