package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.auth.AuthenticationRequest;
import com.highschool.highschoolsystem.auth.AuthenticationResponse;
import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request,
            @RequestParam(value = "role", required = true, defaultValue = "student") String role
            ) {
        return ResponseEntity.ok(authenticationService.register(request, role));
    }
}