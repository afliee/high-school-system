package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.ScheduleConverter;
import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.repository.LessonRepository;
import com.highschool.highschoolsystem.repository.LevelRepository;
import com.highschool.highschoolsystem.repository.ScheduleRepository;
import com.highschool.highschoolsystem.service.ScheduleService;
import com.highschool.highschoolsystem.service.SubjectService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/schedule")
@PreAuthorize("hasRole('ADMIN')")
public class ScheduleApiController {
    private static final Logger LOGGER = Logger.getLogger(ScheduleApiController.class.getName());
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ScheduleService scheduleService;
    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects(
            @RequestParam(value = "level", defaultValue = "") String levelId
    ) {
        var subjectGroupByResponseMap = scheduleService.getSubjectGroupedByLevel(levelId);
        return new ResponseEntity<>(subjectGroupByResponseMap, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody @Valid SchedulingRequest request
    ) {
        if (request.getLessonIds().isEmpty()) {
            return new ResponseEntity<>("LessonIds is required", HttpStatus.BAD_REQUEST);
        }

        if (request.getSubjectIds().isEmpty()) {
            return new ResponseEntity<>("SubjectIds is required", HttpStatus.BAD_REQUEST);
        }

        scheduleService.create(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/subject-available")
    public ResponseEntity<?> subjectAvailable(
            @RequestParam(name = "classId", defaultValue = "") String classId
    ) {

        var subjectAvailable = scheduleService.getSubjectAvailable(classId);
        return new ResponseEntity<>(subjectAvailable, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> scheduleDetail(
            @RequestParam(name = "classId", defaultValue = "") String classId,
            @RequestParam(name = "semesterId", defaultValue = "") String semesterId,
            @RequestParam(name = "start", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NonNull LocalDate start,
            @RequestParam(name = "end", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NonNull LocalDate end
    ) {
        var scheduleDetail = scheduleService.getScheduleDetail(classId, semesterId, start, end);
        return new ResponseEntity<>(scheduleDetail, HttpStatus.OK);
    }
}
