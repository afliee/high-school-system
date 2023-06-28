package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping({"/", ""})
    public String index() {
        return "Admin";
    }

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
