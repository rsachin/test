package com.example.propertylisting.controller;

import com.example.propertylisting.dto.AuthenticationRequest;
import com.example.propertylisting.dto.AuthenticationResponse;
import com.example.propertylisting.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        logger.debug("Received authentication request with email: {}", request.email());
        logger.debug("Attempting to authenticate user");
        return ResponseEntity.ok(service.authenticate(request));
    }
}
