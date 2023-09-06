package com.highschool.highschoolsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        // Arrange
        String component = "dashboard";

        // Act
        String result = homeController.index(component, model);

        // Assert
        assertEquals("pages/dashboard", result);
        verify(model).addAttribute("title", "Home");
    }

    @Test
    void testIndexWithEmptyComponent() {
        // Arrange
        String component = "";

        // Act
        String result = homeController.index(component, model);

        // Assert
        assertEquals("index", result);
        verify(model).addAttribute("title", "Home");
    }

    @Test
    void testLogin() {
        // Arrange
        String role = "admin";

        // Act
        String result = homeController.login(role, model);

        // Assert
        assertEquals("pages/auth/signin", result);
        verify(model).addAttribute("title", "Sign In");
        verify(model).addAttribute("role", role);
    }

    @Test
    void testLoginWithEmptyRole() {
        // Arrange
        String role = "";

        // Act
        String result = homeController.login(role, model);

        // Assert
        assertEquals("404", result);
        verify(model).addAttribute("title", "Sign In");
    }

    @Test
    void testRegister() {
        // Arrange
        String role = "student";

        // Act
        String result = homeController.register(role, model);

        // Assert
        assertEquals("pages/auth/signup", result);
        verify(model).addAttribute("title", "Sign Up");
        verify(model).addAttribute("role", role);
    }

    @Test
    void testRegisterWithEmptyRole() {
        // Arrange
        String role = "";

        // Act
        String result = homeController.register(role, model);

        // Assert
        assertEquals("404", result);
        verify(model).addAttribute("title", "Sign Up");
    }

    @Test
    void testForgotPassword() {
        // Act
        String result = homeController.forgotPassword(model);

        // Assert
        assertEquals("pages/auth/forgot", result);
        verify(model).addAttribute("title", "Forgot Password");
    }
}