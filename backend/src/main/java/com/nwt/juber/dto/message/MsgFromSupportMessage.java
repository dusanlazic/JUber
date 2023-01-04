package com.nwt.juber.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MsgFromSupportMessage {
    private String content;
    private Date sentAt;
}
