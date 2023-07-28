package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.DayConverter;
import com.highschool.highschoolsystem.converter.LessonConverter;
import com.highschool.highschoolsystem.converter.ShiftConverter;
import com.highschool.highschoolsystem.converter.SubjectConverter;
import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.dto.response.SchedulingResponse;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.dto.response.SubjectGroupByResponse;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.LessonRepository;
import com.highschool.highschoolsystem.repository.LevelRepository;
import com.highschool.highschoolsystem.service.LessonService;
import com.highschool.highschoolsystem.service.ScheduleService;
import com.highschool.highschoolsystem.service.SubjectService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        var level = levelRepository.findById(levelId).orElseThrow(
                () -> new NotFoundException("Level not found")
        );

        var subjects = SubjectConverter.toResponse(level.getSubjects().stream().toList());
        Map<String, SubjectGroupByResponse> subjectGroupByResponseMap = new HashMap<>();
        subjects.forEach(subject -> {
            var lessons = lessonRepository.findAllBySubjectId(subject.getId());

            var subjectByLevelResponse = SchedulingResponse.SubjectByLevelResponse.builder()
                    .id(subject.getId())
                    .name(subject.getName())
                    .teacher(subject.getTeacher())
                    .color(subject.getColor())
                    .lessons(lessons.stream().map(lesson -> {
                        return LessonResponse.builder()
                                .id(lesson.getId())
                                .shift(ShiftConverter.toResponse(lesson.getShift()))
                                .day(DayConverter.toResponse(lesson.getDay()))
                                .startDate(lesson.getStartDate())
                                .endDate(lesson.getEndDate())
                                .build();
                    }).toList())
                    .build();
            if (!subjectGroupByResponseMap.containsKey(subject.getDepartmentDetail().getId())) {
                var subjectGroupByResponse = SubjectGroupByResponse.builder()
                        .name(subject.getDepartment())
                        .subjects(new ArrayList<>(List.of(subjectByLevelResponse)))
                        .build();
                subjectGroupByResponseMap.put(subject.getDepartmentDetail().getId(), subjectGroupByResponse);
            } else {
                var subjectGroupByResponse = subjectGroupByResponseMap.get(subject.getDepartmentDetail().getId());
                subjectGroupByResponse.getSubjects().add(subjectByLevelResponse);
            }
        });

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
