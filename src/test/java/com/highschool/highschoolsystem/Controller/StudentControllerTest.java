package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.entity.NavigatorEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.service.JwtService;
import com.highschool.highschoolsystem.service.NavigatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.util.WebUtils;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Model model;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private NavigatorService navigatorService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        Cookie tokenCookie = mock(Cookie.class);
        when(WebUtils.getCookie(request, "token")).thenReturn(tokenCookie);
        when(tokenCookie.getValue()).thenReturn("token-value");

        StudentEntity student = new StudentEntity();
        when(studentRepository.findById("student-id")).thenReturn(Optional.of(student));

        // Call the method
        String result = studentController.index(request, model);

        // Verify the interactions and assertions
        verify(studentRepository).findById("student-id");
        verify(model).addAttribute("student", student);
        assertEquals("pages/student/index", result);
    }

    @Test
    void testNavigatorRegistration() {
        // Mock the required dependencies
        Cookie tokenCookie = mock(Cookie.class);
        MockedStatic<WebUtils> webUtilsMockedStatic = mockStatic(WebUtils.class);
        webUtilsMockedStatic.when(() -> WebUtils.getCookie(request, "token")).thenReturn(tokenCookie);
        when(tokenCookie.getValue()).thenReturn("token-value");

        NavigatorEntity navigator = NavigatorEntity.builder().build();
        when(navigatorService.findNavigatorAlreadyRegistered("token-value")).thenReturn(navigator);

        // Call the method
        String result = studentController.navigatorRegistration(request, model);

        // Verify the interactions and assertions
        verify(navigatorService).findNavigatorAlreadyRegistered("token-value");
        verify(model).addAttribute("navigator", navigator);
        assertEquals("redirect:/?component=chooseLogin", result);
    }

}