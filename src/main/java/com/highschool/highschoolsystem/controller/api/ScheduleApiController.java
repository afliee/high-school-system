package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.repository.LessonRepository;
import com.highschool.highschoolsystem.repository.LevelRepository;
import com.highschool.highschoolsystem.repository.ScheduleRepository;
import com.highschool.highschoolsystem.service.ScheduleService;
import com.highschool.highschoolsystem.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ScheduleRepository scheduleRepository;

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
        if (request.getLessonIds().size() == 0) {
            return new ResponseEntity<>("LessonIds is required", HttpStatus.BAD_REQUEST);
        }

        scheduleService.create(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
