package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.SubjectRequest;
import com.highschool.highschoolsystem.service.SubjectService;
import jakarta.validation.Valid;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/subject")
public class SubjectApiController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(
                subjectService.get(id)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "filter", defaultValue = "departmentId:0") String filter
    ) {
        return ResponseEntity.ok(
                subjectService.getAll(page, size, sort, sortBy, filter)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody @Valid SubjectRequest subjectRequest
    ) {
        return ResponseEntity.ok(
                subjectService.create(subjectRequest)
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") String id,
            @RequestBody @Valid SubjectRequest subjectRequest
    ) {
        return ResponseEntity.ok(
                subjectService.update(id, subjectRequest)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(
                subjectService.delete(id)
        );
    }
}
