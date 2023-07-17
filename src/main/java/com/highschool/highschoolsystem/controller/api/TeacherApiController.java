package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.TeacherConverter;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.service.TeacherService;
import com.highschool.highschoolsystem.service.TeacherServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
@Tag(name = "Teacher", description = "Teacher API for CRUD")
public class TeacherApiController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping({"/", ""})
    public String index(HttpServletRequest request) {
        return "Teacher";
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(
                TeacherConverter.toResponse(teacherService.findById(id))
        );
    }

    @DeleteMapping("/{id}")
    public String deleteTeacherById(@PathVariable(name = "id") String id) {
        teacherService.deleteById(id);
        return "Teacher deleted successfully";
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacherById(
            @PathVariable(name = "id") String id,
            @RequestBody TeacherResponse teacherResponse
    ) {

        return ResponseEntity.ok(
                TeacherConverter.toResponse(teacherService.updateById(id, teacherResponse))
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTeachers() {
        return ResponseEntity.ok(
                teacherService.findAllTeachers()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> findByName(@RequestParam(name = "q", defaultValue = "") String query) {
        if (query.isEmpty()) {
            return ResponseEntity.ok(
                    teacherService.findAllTeachers()
            );
        }

        return ResponseEntity.ok(
                teacherService.findByName(query)
        );
    }
}
