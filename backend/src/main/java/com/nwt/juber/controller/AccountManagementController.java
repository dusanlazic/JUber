package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.BlockedUserNoteUpdateRequest;
import com.nwt.juber.dto.request.ProfileInfoChangeResolveRequest;
import com.nwt.juber.dto.response.BlockedUserResponse;
import com.nwt.juber.dto.response.ProfileChangeRequestResponse;
import com.nwt.juber.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AccountManagementController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/change-requests")
    public List<ProfileChangeRequestResponse> getProfileChangeRequests() {
        return accountService.getPendingProfileChangeRequests();
    }

    @PatchMapping("/change-requests/{requestId}/resolve")
    public ResponseOk resolveProfileChangeRequest(@PathVariable UUID requestId, @RequestBody @Valid ProfileInfoChangeResolveRequest resolveRequest) {
        return accountService.resolveProfileChangeRequest(requestId, resolveRequest);
    }

    @GetMapping("/blocked-users")
    public List<BlockedUserResponse> getBlockedUsers() {
        return accountService.getBlockedUsers();
    }

    @DeleteMapping("/blocked-users/{userId}")
    public ResponseOk unblockUser(@PathVariable UUID userId) {
        return accountService.unblockUser(userId);
    }

    @PatchMapping("/blocked-users/{userId}/note")
    public ResponseOk updateNote(@PathVariable UUID userId, @RequestBody @Valid BlockedUserNoteUpdateRequest noteUpdateRequest) {
        return accountService.updateNote(userId, noteUpdateRequest);
    }

    @PostMapping("/blocked-users/{userEmail}")
    public BlockedUserResponse blockUser(@PathVariable String userEmail) {
        return accountService.blockUser(userEmail);
    }
}
