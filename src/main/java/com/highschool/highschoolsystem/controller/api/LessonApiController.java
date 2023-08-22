package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.LessonRequest;
import com.highschool.highschoolsystem.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonApiController {
    @Autowired
    private LessonService lessonService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody @Valid LessonRequest request
    ) {
        if (request.getDayIds().size() != request.getShiftIds().size()) {
            return ResponseEntity.badRequest().body("Day and shift must be the same size");
        }
        return ResponseEntity.ok(lessonService.create(request));
    }

    @GetMapping("/get")
    public List<?> get(
            @RequestParam(name = "semesterId" , defaultValue = "current") String semesterId,
            @RequestParam String subjectId
    ) {
        return lessonService.get(semesterId, subjectId);
    }

    @GetMapping("/get/{subjectId}")
    public List<?> get(
            @PathVariable String subjectId,
            @RequestParam String semesterId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(defaultValue = "false") boolean isDetail
    ) {
        return lessonService.get(subjectId, semesterId, start, end, isDetail);
    }
}
