package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.AttendanceConverter;
import com.highschool.highschoolsystem.dto.request.AttendanceRequest;
import com.highschool.highschoolsystem.service.AttendanceService;
import com.highschool.highschoolsystem.service.NavigatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceApiController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private NavigatorService navigatorService;

    @PostMapping("/submit")
    public ResponseEntity<?> submit(
            @RequestBody @Valid AttendanceRequest attendanceRequest,
            HttpServletRequest request
    ) {

        var requireStudent = attendanceService.requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return ResponseEntity.badRequest().body(requireStudent);
        }

        var navigator = navigatorService.getNavigator(requireStudent);
        attendanceService.submit(navigator , attendanceRequest);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> index(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        Cookie cookie = WebUtils.getCookie(request, "token");
        var requireStudent = attendanceService.requireStudent(cookie);
        if (requireStudent.startsWith("redirect")) {
            return ResponseEntity.badRequest().body(requireStudent);
        }

        return ResponseEntity.ok().body(attendanceService.getAttendance(id));
    }
}
