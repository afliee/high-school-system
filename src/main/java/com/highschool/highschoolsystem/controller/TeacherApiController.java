package com.highschool.highschoolsystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacher")
@PreAuthorize("hasRole('ROLE_TEACHER')")
@Tag(name = "Teacher", description = "Teacher API for CRUD")
public class TeacherApiController {
    @GetMapping({"/", ""})
    public String index(HttpServletRequest request) {

        return "Teacher";
    }
}
