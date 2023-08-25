package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.ScheduleConverter;
import com.highschool.highschoolsystem.dto.request.UpdateStudentRequest;
import com.highschool.highschoolsystem.service.ScheduleService;
import com.highschool.highschoolsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
public class StudentApiController {
    @Autowired
    private ScheduleService scheduleService;
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

    @GetMapping("/schedule/{id}")
    public ResponseEntity<?> index(
            @PathVariable("id") String id
    ) {
        var schedule = scheduleService.findById(id).orElse(null);
        return new ResponseEntity<>(ScheduleConverter.toResponse(schedule), HttpStatus.OK);
    }

}
