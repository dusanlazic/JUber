package com.nwt.juber.controller;

import com.nwt.juber.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/uploads")
public class FileStorageController {

    @Autowired
    private FileStorageService storageService;

    @GetMapping(value = "/{filename}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Resource> serve(@PathVariable String filename) throws IOException {
        return storageService.serve(filename);
    }

}
