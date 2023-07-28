package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.LevelRequest;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.service.LevelService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/level")
public class LevelApiController {
    @Autowired
    private LevelService levelService;

    @GetMapping("/all")
    public ResponseEntity<List<LevelResponse>> getAll(
            @RequestParam(name = "isDetail", defaultValue = "false") boolean isDetail
    ) {
        return ResponseEntity.ok(levelService.findAll(isDetail));
    }

    @PostMapping("/create")
    public ResponseEntity<LevelResponse> create(
            @RequestBody @Valid LevelRequest levelRequest
    ) {
         return ResponseEntity.ok(levelService.create(levelRequest));
    }
}
