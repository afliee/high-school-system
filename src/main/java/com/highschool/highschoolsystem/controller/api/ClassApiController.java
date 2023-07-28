package com.highschool.highschoolsystem.controller.api;

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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class") // http://localhost:8080/api/v1/classes
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class ClassApiController {
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
    public ResponseEntity<?> add(
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

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(
            @PathVariable String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size
    ) {
        return new ResponseEntity<>(classService.get(id, page, size), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<?>> get(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "semesterId", defaultValue = "current") String semesterId,
            @RequestParam(name = "levelId", defaultValue = "") String levelId
    ) {
        if (!levelId.isEmpty()) {
            return new ResponseEntity<>(classService.get(page, size, semesterId, levelId), HttpStatus.OK);
        }

        return new ResponseEntity<>(classService.get(page, size, semesterId), HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestParam(name = "studentId", defaultValue = "") String studentId) {
        classService.delete(id, studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam String classId
    ) {
        classService.delete(classId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/set-chairman")
    public ResponseEntity<?> setChairman(@RequestParam String classId, @RequestParam String teacherId) {
        classService.setChairman(classId, teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
