package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.UpdateStudentRequest;
import com.highschool.highschoolsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
public class StudentApiController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntity.ok(studentService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @ModelAttribute @Valid UpdateStudentRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }
}
