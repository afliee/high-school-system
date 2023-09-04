package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.TeacherConverter;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeacherApiControllerTest {
    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherApiController teacherApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTeacherById() {
        // Mock the behavior of the teacherService.findById() method
        String id = "123";
        TeacherEntity expectedResponse = TeacherEntity.builder().build();
        when(teacherService.findById(id)).thenReturn(expectedResponse);

        // Call the getTeacherById() method of the teacherApiController
        ResponseEntity<TeacherResponse> responseEntity = teacherApiController.getTeacherById(id);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}