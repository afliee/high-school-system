package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.converter.TeacherConverter;
import com.highschool.highschoolsystem.converter.UserConverter;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.util.BreadCrumb;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AdminService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public String requireAdminLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies == null) {
            return "redirect:/auth/admin/login";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }

        if (token == null) {
            return "redirect:/auth/admin/login";
        }

        String username = jwtService.extractUsername(token);
        if (username == null) {
            return "redirect:/auth/admin/login";
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        var isTokenValid = tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (!jwtService.isAdmin(token) || !isTokenValid || !jwtService.isTokenValid(token, UserConverter.toPrincipal(user))) {
            return "redirect:/auth/admin/login";
        }

        System.out.println("token valid: " + jwtService.isTokenValid(token, UserConverter.toPrincipal(user)));
        System.out.println("token valid 1: " + isTokenValid);
        System.out.println("token admin: " + jwtService.isAdmin(token));
        System.out.println("token: " + token);
        return null;
    }

    public String requireAdminLogin(String token) {
        if (!jwtService.validateToken(token) && !jwtService.isAdmin(token)) {
            return "redirect:/auth/admin/login";
        }

        return null;
    }

    public Page<?> getAll(String filter, int page) {
        final int PAGE_SIZE = 10;

        if (filter.equals("teacher")) {
            PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
//            return teacherRepository.findAll(pageRequest);
            return teacherRepository.findAll(pageRequest).map(TeacherConverter::toResponse);
        }
        return null;
    }

    public TeacherResponse createTeacher(RegistrationRequest request) {
        try {
            System.out.println(request);
            authenticationService.register(request, "teacher");
            return TeacherResponse.builder()
                    .name(request.getUsername())
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BreadCrumb> getDepartmentBreadCrumbs(String departmentId) {
        return List.of(
                BreadCrumb.builder()
                        .name("Dashboard")
                        .url("/auth/admin")
                        .isActive(false)
                        .build(),
                BreadCrumb.builder()
                        .name("Department")
                        .url("/auth/admin/department")
                        .isActive(false)
                        .build(),
                BreadCrumb.builder()
                        .name(departmentId)
                        .url("/auth/admin/department/" + departmentId)
                        .isActive(true)
                        .build()
        );
    }

    public List<BreadCrumb> getSubjectBreadCrumbs(String s) {
        if (s.isEmpty()) {
            return List.of(
                    BreadCrumb.builder()
                            .name("Dashboard")
                            .url("/auth/admin")
                            .isActive(false)
                            .build(),
                    BreadCrumb.builder()
                            .name("Subject")
                            .url("/auth/admin/subjects")
                            .isActive(true)
                            .build()
            );
        } else {
            return List.of(
                    BreadCrumb.builder()
                            .name("Dashboard")
                            .url("/auth/admin")
                            .isActive(false)
                            .build(),
                    BreadCrumb.builder()
                            .name("Subject")
                            .url("/auth/admin/subjects")
                            .isActive(false)
                            .build(),
                    BreadCrumb.builder()
                            .name(s)
                            .url("/auth/admin/subjects/" + s)
                            .isActive(true)
                            .build()
            );
        }
    }

    public List<BreadCrumb> getLevelBreadCrumbs(String levelId) {
        return List.of(
                BreadCrumb.builder()
                        .name("Dashboard")
                        .url("/auth/admin")
                        .isActive(false)
                        .build(),
                BreadCrumb.builder()
                        .name("Level")
                        .url("/auth/admin/level")
                        .isActive(true)
                        .build(),
                BreadCrumb.builder()
                        .name(levelId)
                        .url("/auth/admin/level/" + levelId)
                        .isActive(true)
                        .build()
        );
    }

    public List<BreadCrumb> getSemesterBreadCrumbs(String semesterId) {
        return List.of(
                BreadCrumb.builder()
                        .name("Dashboard")
                        .url("/auth/admin")
                        .isActive(false)
                        .build(),
                BreadCrumb.builder()
                        .name("Semester")
                        .url("/auth/admin/semester")
                        .isActive(true)
                        .build(),
                BreadCrumb.builder()
                        .name(semesterId)
                        .url("/auth/admin/semester/" + semesterId)
                        .isActive(true)
                        .build()
        );
    }

    public List<BreadCrumb> getScheduleDetail(String classId) {
        return List.of(
                BreadCrumb.builder()
                        .name("Dashboard")
                        .url("/auth/admin")
                        .isActive(false)
                        .build(),
                BreadCrumb.builder()
                        .name("Schedule")
                        .url("/auth/admin/review-scheduling")
                        .isActive(false)
                        .build(),
                BreadCrumb.builder()
                        .name(classId)
                        .url("/auth/admin/review-scheduling/" + classId)
                        .isActive(true)
                        .build()
        );
    }
}
