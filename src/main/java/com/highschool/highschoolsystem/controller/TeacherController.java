package com.highschool.highschoolsystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
@Tag(name = "Teacher", description = "Teacher views for CRUD")
public class TeacherController {
    @GetMapping({"/home", "/", ""})
    public String index() {
        return "Teacher";
    }
}
