package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.auth.*;
import com.highschool.highschoolsystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticate user with username and password and return JWT token for further requests",
            tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication request",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthenticationRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Bad credentials")
            }

    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request,
            @NotNull HttpServletRequest httpServletRequest,
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request, httpServletRequest, httpServletResponse));
    }

    @Operation(
            summary = "Register user",
            description = "Register user with username, password and role",
            tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration request",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RegistrationRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful"),
                    @ApiResponse(responseCode = "400", description = "User already exists")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegistrationRequest request,
            @RequestParam(value = "role", required = true, defaultValue = "") String role
    ) {
        return ResponseEntity.ok(authenticationService.register(request, role));
    }


    @Operation(
            summary = "Refresh token",
            description = "Refresh JWT token",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed"),
                    @ApiResponse(responseCode = "500", description = "Refresh token is expired or invalid")
            }
    )
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthenticationResponse> resetPassword(
            HttpServletRequest request,
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(request, resetPasswordRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(authenticationService.forgotPassword(request));
    }

    @PostMapping("/forgot-password/confirm")
    public ResponseEntity<AuthenticationResponse> forgotPassword(
            @RequestBody @Valid ForgotConfirmRequest request
    ) {
        return ResponseEntity.ok(authenticationService.forgotPasswordConfirm(request));
    }
}
