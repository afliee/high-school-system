package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.ShiftRequest;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shift")
public class ShiftApiController {
    @Autowired
    private ShiftService shiftService;

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

}
