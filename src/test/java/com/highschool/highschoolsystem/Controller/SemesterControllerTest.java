package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.converter.SemesterConverter;
import com.highschool.highschoolsystem.dto.request.SemesterRequest;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.service.SemesterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SemesterControllerTest {

    @Mock
    private SemesterService semesterService;

    @InjectMocks
    private SemesterController semesterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        // Arrange
        List<SemesterResponse> semesters = new ArrayList<>();
        when(semesterService.findAll()).thenReturn(semesters);

        // Act
        ResponseEntity<?> response = semesterController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(semesters, response.getBody());
        verify(semesterService).findAll();
    }

    @Test
    void testAdd() {
        // Arrange
        SemesterRequest request = new SemesterRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 6, 30));
        SemesterEntity semester = SemesterConverter.toEntity(request);
        when(semesterService.save(any(SemesterEntity.class))).thenReturn(SemesterResponse.builder().build());

        // Act
        ResponseEntity<?> response = semesterController.add(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(semester, response.getBody());
        verify(semesterService).save(semester);
    }

    @Test
    void testAddWithInvalidDates() {
        // Arrange
        SemesterRequest request = new SemesterRequest();
        request.setStartDate(LocalDate.of(2022, 1, 1));
        request.setEndDate(LocalDate.of(2021, 12, 31));

        // Act
        ResponseEntity<?> response = semesterController.add(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Start date or end date is invalid", response.getBody());
        verify(semesterService, never()).save(any(SemesterEntity.class));
    }

    @Test
    void testUpdate() {
        // Arrange
        String id = "semesterId";
        SemesterRequest request = new SemesterRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 6, 30));
        SemesterEntity semester = SemesterConverter.toEntity(request);
        when(semesterService.update(eq(id), any(SemesterRequest.class))).thenReturn(SemesterResponse.builder().build());

        // Act
        ResponseEntity<?> response = semesterController.update(id, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(semester, response.getBody());
        verify(semesterService).update(id, request);
    }

    @Test
    void testUpdateWithInvalidDates() {
        // Arrange
        String id = "semesterId";
        SemesterRequest request = new SemesterRequest();
        request.setStartDate(LocalDate.of(2022, 1, 1));
        request.setEndDate(LocalDate.of(2022, 6, 30));

        // Act
        ResponseEntity<?> response = semesterController.update(id, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Start date or end date is invalid", response.getBody());
        verify(semesterService, never()).update(eq(id), any(SemesterRequest.class));
    }

    @Test
    void testDelete() {
        // Arrange
        String id = "semesterId";

        // Act
        ResponseEntity<?> response = semesterController.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete success", response.getBody());
        verify(semesterService).delete(id);
    }
}