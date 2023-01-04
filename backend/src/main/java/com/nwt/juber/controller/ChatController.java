package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.ChatMessageRequest;
import com.nwt.juber.dto.response.ChatConversationResponse;
import com.nwt.juber.dto.response.ChatMessageResponse;
import com.nwt.juber.exception.NotImplementedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/support")
public class ChatController {

    @GetMapping("/chat")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public List<ChatMessageResponse> getMessagesAsUser(Authentication authentication) {
        throw new NotImplementedException();
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk sendMessageAsUser(@RequestBody @Valid ChatMessageRequest messageRequest, Authentication authentication) {
        throw new NotImplementedException();
    }

    @GetMapping("/admin/users/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChatConversationResponse> getConversations(Authentication authentication) {
        throw new NotImplementedException();
    }

    @GetMapping("/admin/users/{userId}/chat")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChatMessageResponse> getMessagesAsSupport(Authentication authentication, @PathVariable UUID userId) {
        throw new NotImplementedException();
    }

    @PostMapping("/admin/users/{userId}/chat")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk sendMessageAsSupport(@RequestBody @Valid ChatMessageRequest messageRequest,
                                           @PathVariable UUID userId,
                                           Authentication authentication) {
        throw new NotImplementedException();
    }

}
