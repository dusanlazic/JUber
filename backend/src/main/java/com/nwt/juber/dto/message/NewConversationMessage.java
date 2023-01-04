package com.nwt.juber.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NewConversationMessage {
    private UUID userId;
}
