package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
