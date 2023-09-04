package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.auth.AuthenticationResponse;
import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.service.AdminService;
import com.highschool.highschoolsystem.service.AuthenticationService;
import com.highschool.highschoolsystem.service.JwtService;
import com.highschool.highschoolsystem.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AdminServiceTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthenticationService authenticationService;

    private AdminService adminService;

   @BeforeEach
    void setUp() {
       MockitoAnnotations.openMocks(this);
       adminService = new AdminService(jwtService, teacherRepository, userRepository, tokenRepository, authenticationService);
    }

    @Test
    void createTeacher_shouldReturnTeacherResponse() {
        TeacherResponse teacherResponse = new TeacherResponse();
        RegistrationRequest registrationRequest = new RegistrationRequest();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        when(authenticationService.register(registrationRequest, "teacher")).thenReturn(authenticationResponse);

        TeacherResponse teacherResponse1 = adminService.createTeacher(registrationRequest);

        assertEquals(teacherResponse, teacherResponse1);
        verify(authenticationService, times(1)).register(registrationRequest, "teacher");
    }
}
