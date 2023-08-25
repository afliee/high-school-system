package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.service.SubmittingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/submitting")
@RequiredArgsConstructor
public class SubmittingApiController {
    private final SubmittingService submittingService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmitting(
            @PathVariable String id
    ) {
        return ResponseEntity.ok().body(submittingService.getSubmitting(id));
    }
}
