package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.PasswordChangeRequest;
import com.nwt.juber.dto.request.ProfileInfoChangeRequest;
import com.nwt.juber.dto.response.PhotoUploadResponse;
import com.nwt.juber.dto.response.ProfileInfoResponse;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.User;
import com.nwt.juber.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public PhotoUploadResponse setProfilePicture(@RequestParam("file") MultipartFile file, Authentication authentication) {
        return accountService.setProfilePicture(file, authentication);
    }

    @DeleteMapping(value = "/profile-picture")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk removeProfilePicture(Authentication authentication) {
        accountService.removeProfilePicture(authentication);
        return new ResponseOk("Profile picture removed.");
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ProfileInfoResponse getProfileInfo(Authentication authentication) {
        return accountService.getProfileInfo(authentication);
    }

    @PatchMapping(value = "/me")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk updateProfileInfo(@RequestBody @Valid ProfileInfoChangeRequest profileInfo, Authentication authentication) {
        return accountService.updateProfileInfo(profileInfo, authentication);
    }
    
    @PatchMapping(value = "/change-password")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk changePassword(@RequestBody @Valid PasswordChangeRequest passwordChangeRequest, Authentication authentication) {
    	User user = (User) authentication.getPrincipal();
    	accountService.changePassword(passwordChangeRequest, user);
        return new ResponseOk("Password successfully changed.");
    }

}
