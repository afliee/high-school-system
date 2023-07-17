package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Admin", description = "Admin API for CRUD")
public class AdminApiController {
    @Autowired
    private AdminService adminService;

    @GetMapping("")
    public ResponseEntity<?> index(@RequestParam(name = "token") String token) {
        String redirect = adminService.requireAdminLogin(token);
        if (redirect != null) {
            return ResponseEntity.badRequest().body(redirect);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new teacher by admin",
            description = "Create a new teacher by admin",
            tags = "Admin",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Teacher object that will be created",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RegistrationRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teacher created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "500", description = "Bad credentials")
            }
        )
    @PostMapping("/teacher")
    public ResponseEntity<TeacherResponse> createTeacher(
            @RequestBody @Valid RegistrationRequest request
            )
    {
        return ResponseEntity.ok(adminService.createTeacher(request));
    }


    @GetMapping("/members")
    public ResponseEntity<Page<?>> getAll(
            @RequestParam(name = "filter", required = true, defaultValue = "") String filter,
            @RequestParam(name = "page", required = true, defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(adminService.getAll(filter, page));
    }
}
