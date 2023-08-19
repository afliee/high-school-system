package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.service.ClassService;
import com.highschool.highschoolsystem.service.JwtService;
import com.highschool.highschoolsystem.service.StudentService;
import com.highschool.highschoolsystem.service.TeacherService;
import com.highschool.highschoolsystem.util.BreadCrumb;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
@Tag(name = "Teacher", description = "Teacher views for CRUD")
public class TeacherController {
    private final JwtService jwtService;
    private final TeacherService teacherService;
    private final ClassService classService;
    private final StudentService studentService;

    @GetMapping({"/home", "/", ""})
    public String index(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");
        if (tokenCookie == null || tokenCookie.getValue().isEmpty()) {
            return "redirect:/login?with=teacher";
        }

        if (!jwtService.validateToken(tokenCookie.getValue())) {
            return "redirect:/login?with=teacher";
        }

        if (!jwtService.isTeacher(tokenCookie.getValue())) {
            return "redirect:/login?with=teacher";
        }

        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        model.addAttribute("teacher", teacher);
        return "pages/teacher/index";
    }

    @GetMapping("/form-teacher")
    public String formTeacher(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {

        var tokenCookie = WebUtils.getCookie(request, "token");
        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var classEntity = classService.findByTeacher(teacher.getId());
        if (classEntity == null) {
            return "redirect:/?component=chooseLogin";
        }

        model.addAttribute("classEntity", classEntity);
        model.addAttribute("teacher", teacher);
        return "pages/teacher/form-teacher";
    }

    @GetMapping("/form-teacher/{studentId}")
    public String studentDetail(
            @PathVariable String studentId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var student = studentService.findOne(studentId);

        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(student.getFullName(), "/teacher/form-teacher/" + studentId, true));

        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("student", student);

        return "pages/teacher/student-detail";
    }

    private List<BreadCrumb> generateBreadCrumbs() {
        var breadCrumbs = new ArrayList<BreadCrumb>();
        breadCrumbs.add(new BreadCrumb("Home", "/teacher/home", false));
        breadCrumbs.add(new BreadCrumb("Teacher", "/teacher/form-teacher", false));
        return breadCrumbs;
    }
}
