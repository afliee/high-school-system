package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.ShiftRequest;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.service.DayService;
import com.highschool.highschoolsystem.service.LessonService;
import com.highschool.highschoolsystem.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shift")
public class ShiftApiController {
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private DayService dayService;

    @PostMapping("/create")
    public ResponseEntity<ShiftResponse> create(
            @RequestBody @Valid ShiftRequest shiftRequest
    ) {
        return ResponseEntity.ok(shiftService.create(shiftRequest));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ShiftResponse>> getAll() {
        return ResponseEntity.ok(shiftService.getAll());
    }

    @GetMapping("/available-time")
    public ResponseEntity<?> getAvailableTime(
            @RequestParam(name = "subjectId") String subjectId,
            @RequestParam(name = "semesterId") String semesterId
    ) {
        return ResponseEntity.ok(shiftService.getAvailableTime(subjectId, semesterId));
    }
}
