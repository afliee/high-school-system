package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.dto.request.AddClassRequest;
import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.service.ClassService;
import com.highschool.highschoolsystem.util.spreadsheet.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class") // http://localhost:8080/api/v1/classes
@PreAuthorize("hasRole('ADMIN')")
public class ClassController {
    @Autowired
    private ClassService classService;

    @Operation(
            summary = "Add new class",
            description = "This API is used to add new class",
            tags = {"Class"},
            requestBody = @RequestBody(
                    description = "Class object that will be added",
                    content = {
                            @Content(mediaType = "multipart/form-data", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AddClassRequest.class))
                    }
            )
    )
    @PostMapping("/add")
    public ResponseEntity<ClassEntity> add(
            @ModelAttribute @Valid AddClassRequest request
    ) {
        if (ExcelUtil.isExcel(request.getStudents())) {
            return new ResponseEntity<>(classService.save(request), HttpStatus.OK);
        } else throw new RuntimeException("File is not excel: " + request.getStudents().getOriginalFilename());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        String fileName = "classes.xlsx";
        InputStreamResource file = new InputStreamResource(classService.download(id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
