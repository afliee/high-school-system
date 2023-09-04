package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.ScheduleConverter;
import com.highschool.highschoolsystem.dto.request.UpdateStudentRequest;
import com.highschool.highschoolsystem.dto.response.ScheduleResponse;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.ScheduleEntity;
import com.highschool.highschoolsystem.service.ScheduleService;
import com.highschool.highschoolsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentApiControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentApiController studentApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get_shouldReturnResponseEntityWithStudent() {
        // Arrange
        String studentId = "123";
        StudentResponse student = StudentResponse.builder().build();
        when(studentService.get(studentId)).thenReturn(student);

        // Act
        ResponseEntity<?> responseEntity = studentApiController.get(studentId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(student, responseEntity.getBody());
        verify(studentService, times(1)).get(studentId);
    }

    @Test
    void update_shouldReturnResponseEntityWithUpdatedStudent() {
        // Arrange
        String studentId = "123";
        UpdateStudentRequest request = new UpdateStudentRequest();
        StudentResponse updatedStudent = StudentResponse.builder().build();
        when(studentService.update(studentId, request)).thenReturn(updatedStudent);

        // Act
        ResponseEntity<?> responseEntity = studentApiController.update(studentId, request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedStudent, responseEntity.getBody());
        verify(studentService, times(1)).update(studentId, request);
    }

    @Test
    void index_shouldReturnResponseEntityWithScheduleResponse() {
        // Arrange
        String scheduleId = "123";
        Object schedule = new Object();
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().build();
        when(scheduleService.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(ScheduleConverter.toResponse(scheduleEntity)).thenReturn(ScheduleResponse.builder().build());

        // Act
        ResponseEntity<?> responseEntity = studentApiController.index(scheduleId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(scheduleEntity, responseEntity.getBody());
        verify(scheduleService, times(1)).findById(scheduleId);
    }
}