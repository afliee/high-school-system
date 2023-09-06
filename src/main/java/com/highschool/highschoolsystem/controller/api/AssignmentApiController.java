package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.dto.request.GradingRequest;
import com.highschool.highschoolsystem.dto.request.SubmitRequest;
import com.highschool.highschoolsystem.service.AssignmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assignment")
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentApiController {
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @ModelAttribute @Valid CreateAssignmentRequest assignmentCreateRequest
    ) {
        assignmentService.create(assignmentCreateRequest);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") String id,
            @ModelAttribute @Valid CreateAssignmentRequest assignmentCreateRequest
    ) {
        return ResponseEntity.ok().body(assignmentService.update(id, assignmentCreateRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") String id
    ) {
        assignmentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<?> submit(
            @PathVariable("id") String id,
            @ModelAttribute @Valid SubmitRequest request
    ) {

        assignmentService.submit(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/re-submit/{id}")
    public ResponseEntity<?> reSubmit(
            @PathVariable("id") String id,
            @ModelAttribute @Valid SubmitRequest request
    ) {
        assignmentService.reSubmit(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/grade/{submittingId}")
    public ResponseEntity<?> grade(
            @PathVariable("submittingId") String submittingId,
            @RequestBody @Valid GradingRequest request
    ) {
        return ResponseEntity.ok().body(assignmentService.grading(submittingId, request));
    }
}
