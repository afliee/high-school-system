package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.auth.*;
import com.highschool.highschoolsystem.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegistrationRequest request,
            @RequestParam(value = "role", required = true, defaultValue = "") String role
    ) {
        return ResponseEntity.ok(authenticationService.register(request, role));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthenticationResponse> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(request));
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {
        authenticationService.forgotPassword(request);
    }

    @PostMapping("/forgot-password/confirm")
    public ResponseEntity<AuthenticationResponse> forgotPassword(
            @RequestBody @Valid ForgotConfirmRequest request
    ) {
        return ResponseEntity.ok(authenticationService.forgotPasswordConfirm(request));
    }
}
