package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.service.*;
import com.highschool.highschoolsystem.util.BreadCrumb;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private NavigatorService navigatorService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectService subjectService;

    @GetMapping({"/home", "/", ""})
    public String index(
            HttpServletRequest request,
            Model model
    ) {
        String requireStudent = requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(WebUtils.getCookie(request, "token").getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }

        var schedule = student.getClassEntity().getSchedule();
        if (schedule != null || !schedule.isExpired() || !schedule.getSubjects().isEmpty()) {
            model.addAttribute("subjects", schedule.getSubjects());
        } else {
            model.addAttribute("subjects", null);
        }
        model.addAttribute("student", student);
        return "pages/student/index";
    }

    @GetMapping("/navigator-registration")
    public String navigatorRegistration(
            HttpServletRequest request,
            Model model
    ) {
        Cookie token = WebUtils.getCookie(request, "token");
        if (token == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(token.getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }

        if (token.getValue().isEmpty()) {
            return "redirect:/?component=chooseLogin";
        }

        var username = jwtService.extractUsername(token.getValue());
        var user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/?component=chooseLogin";
        }

        var student = studentRepository.findById(user.getUserId()).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        model.addAttribute("student", student);
        model.addAttribute("student", user.getUserId());
        return "pages/student/navigator-registration";
    }

    @GetMapping("/time-table")
    public String timeTable(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String requireStudent = requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(WebUtils.getCookie(request, "token").getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }
        model.addAttribute("student", student);
        return "pages/student/time-table";
    }

    @GetMapping("/subject/{subjectId}")
    public String subject(
            @PathVariable("subjectId") String subjectId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String requireStudent = requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(WebUtils.getCookie(request, "token").getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }

        var subject = subjectService.findByIdEntity(subjectId).orElse(null);
        if (subject == null) {
            return "redirect:/?component=chooseLogin";
        }
        model.addAttribute("subject", subject);
        model.addAttribute("student", student);
        return "pages/student/subject";
    }

    @GetMapping("/profile")
    public String profile(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String requireStudent = requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(WebUtils.getCookie(request, "token").getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }
        model.addAttribute("student", student);

        var breadcrums = List.of(
                new BreadCrumb("Home", "/student/home", false),
                new BreadCrumb("Profile", "/student/profile", true)
        );

        model.addAttribute("breadCrumbs", breadcrums);
        return "pages/student/profile";
    }

    @GetMapping("/change-password")
    public String changePassword(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String requireStudent = requireStudent(WebUtils.getCookie(request, "token"));
        if (requireStudent.startsWith("redirect")) {
            return requireStudent;
        }

        var student = studentRepository.findById(requireStudent).orElse(null);
        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(WebUtils.getCookie(request, "token").getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }
        model.addAttribute("student", student);

        var breadcrums = List.of(
                new BreadCrumb("Home", "/student/home", false),
                new BreadCrumb("Change Password", "/student/change-password", true)
        );

        model.addAttribute("breadCrumbs", breadcrums);
        return "pages/student/change-password";
    }

    private String requireStudent(Cookie cookie) {
        if (cookie == null) {
            return "redirect:/?component=chooseLogin";
        }

        var username = jwtService.extractUsername(cookie.getValue());
        var user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/?component=chooseLogin";
        }

        return user.getUserId();
    }
}
