package com.nwt.juber.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChatMessageRequest {

    @NotBlank
    @Size(min = 500, message = "Message size limit exceeded")
    private String content;
}
