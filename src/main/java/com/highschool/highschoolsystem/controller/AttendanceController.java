package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.SemesterRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.service.AttendanceService;
import com.highschool.highschoolsystem.service.JwtService;
import com.highschool.highschoolsystem.service.NavigatorService;
import com.highschool.highschoolsystem.util.BreadCrumb;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final NavigatorService navigatorService;
    private final SemesterRepository semesterRepository;
    private final ClassRepository classRepository;
    private final AttendanceService attendanceService;

    @GetMapping(value = {"", "/home", "/index", "/"})
    public String index(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String requireStudent = attendanceService.requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.getNavigator(student.getId());
        model.addAttribute("navigator", navigator);

        var currentDay = LocalDate.now();

        var semester = semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(currentDay, currentDay);

        if (semester.isEmpty()) {
            model.addAttribute("classes", List.of());
        } else {
            model.addAttribute("classes", semester.get().getClasses().stream().toList());
        }
        model.addAttribute("breadCrumbs", List.of(
                new BreadCrumb("Home", "/", false),
                new BreadCrumb("Attendance", "/attendance", true)
        ));
        model.addAttribute("student", student);
        return "pages/attendance/index";
    }

    @GetMapping("/{classId}")
    public String attendance(
            @PathVariable("classId") String classId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String requireStudent = attendanceService.requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.getNavigator(student.getId());
        model.addAttribute("navigator", navigator);

        var classEntity = classRepository.findById(classId).orElseThrow(
                () -> new NotFoundException("Class not found.")
        );

        model.addAttribute("class", classEntity);
        model.addAttribute("student", student);

        var history = attendanceService.getHistory(classId);
        model.addAttribute("history", history);
        var breadcrumbs = List.of(
                new BreadCrumb("Home", "/", false),
                new BreadCrumb("Attendance", "/attendance", false),
                new BreadCrumb(classEntity.getName(), "/attendance/attendance/" + classEntity.getId(), true)
        );
        model.addAttribute("breadCrumbs", breadcrumbs);
        return "pages/attendance/attendance";
    }

}
