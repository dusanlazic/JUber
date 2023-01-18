package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.ProfileInfoChangeResolveRequest;
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
    public ResponseOk resolveProfileChangeRequest(@RequestBody @PathVariable UUID requestId, @RequestBody @Valid ProfileInfoChangeResolveRequest resolveRequest) {
        return accountService.resolveProfileChangeRequest(requestId, resolveRequest);
    }
}
