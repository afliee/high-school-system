package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.converter.TeacherConverter;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

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

        if (!jwtService.validateToken(token)) {
            return "redirect:/auth/admin/login";
        }

        return null;
    }

    public Page<?> getAll(String filter, int page) {
        final int PAGE_SIZE = 10;

        if (filter.equals("teacher")) {
            PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
            return teacherRepository.findAll(pageRequest);
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
}
