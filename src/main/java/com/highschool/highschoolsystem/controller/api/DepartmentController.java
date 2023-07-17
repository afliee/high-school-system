package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.DepartmentRequest;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.service.DepartmentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody DepartmentRequest departmentRequest
    ) {
        var department = departmentService.save(departmentRequest);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @RequestBody DepartmentRequest departmentRequest
    ) {
        var department = departmentService.update(id, departmentRequest);
        if (department == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(department);
    }

//    get all department
    @GetMapping("/all")
    public ResponseEntity<Page<?>> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {

        return ResponseEntity.ok(departmentService.findAll(page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        var department = departmentService.findById(id);
        if (department == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(department);
    }


}
