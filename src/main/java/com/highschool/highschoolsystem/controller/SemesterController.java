package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.converter.SemesterConverter;
import com.highschool.highschoolsystem.dto.request.SemesterRequest;
import com.highschool.highschoolsystem.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/semester")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(semesterService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody SemesterRequest request) {
        LocalDate currentDay = LocalDate.now();
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

//        convert string to date

        if (start.isBefore(currentDay) || end.isBefore(currentDay)) {
            return ResponseEntity.badRequest().body("Start date or end date is invalid");
        }

        if (start.isAfter(end)) {
            return ResponseEntity.badRequest().body("Start date must be before end date");
        }

        return ResponseEntity.ok(semesterService.save(SemesterConverter.toEntity(request)));
    }
}
