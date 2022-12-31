package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.response.PhotoUploadResponse;
import com.nwt.juber.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PASSENGER_NEW', 'PASSENGER', 'DRIVER')")
    public PhotoUploadResponse setProfilePicture(@RequestParam("file") MultipartFile file, Authentication authentication) {
        return accountService.setProfilePicture(file, authentication);
    }

    @DeleteMapping(value = "/profile-picture")
    @PreAuthorize("hasAnyRole('PASSENGER_NEW', 'PASSENGER', 'DRIVER')")
    public ResponseOk removeProfilePicture(Authentication authentication) {
        accountService.removeProfilePicture(authentication);
        return new ResponseOk("Profile picture removed.");
    }


}
