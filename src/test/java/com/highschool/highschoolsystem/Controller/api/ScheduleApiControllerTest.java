package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.dto.response.SubjectGroupByResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ScheduleApiControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleApiController scheduleApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSubjects_shouldReturnResponseEntityWithSubjectGroupByResponseMap() {
        // Arrange
        String levelId = "123";
        Map<String, SubjectGroupByResponse> subjectGroupByResponseMap = new HashMap<>();
        when(scheduleService.getSubjectGroupedByLevel(levelId)).thenReturn(subjectGroupByResponseMap);

        // Act
        ResponseEntity<?> responseEntity = scheduleApiController.getSubjects(levelId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subjectGroupByResponseMap, responseEntity.getBody());
        verify(scheduleService, times(1)).getSubjectGroupedByLevel(levelId);
    }

    @Test
    void create_shouldReturnBadRequestResponseEntityWhenLessonIdsIsEmpty() {
        // Arrange
        SchedulingRequest request = new SchedulingRequest();
        request.setLessonIds(Collections.emptyList());
        request.setSubjectIds(Collections.singletonList("subject1"));

        // Act
        ResponseEntity<?> responseEntity = scheduleApiController.create(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("LessonIds is required", responseEntity.getBody());
        verify(scheduleService, never()).create(request);
    }

    @Test
    void create_shouldReturnBadRequestResponseEntityWhenSubjectIdsIsEmpty() {
        // Arrange
        SchedulingRequest request = new SchedulingRequest();
        request.setLessonIds(Collections.singletonList("lesson1"));
        request.setSubjectIds(Collections.emptyList());

        // Act
        ResponseEntity<?> responseEntity = scheduleApiController.create(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("SubjectIds is required", responseEntity.getBody());
        verify(scheduleService, never()).create(request);
    }

    @Test
    void create_shouldCallScheduleServiceCreateAndReturnOkResponseEntity() {
        // Arrange
        SchedulingRequest request = new SchedulingRequest();
        request.setLessonIds(Collections.singletonList("lesson1"));
        request.setSubjectIds(Collections.singletonList("subject1"));

        // Act
        ResponseEntity<?> responseEntity = scheduleApiController.create(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
        verify(scheduleService, times(1)).create(request);
    }

    @Test
    void subjectAvailable_shouldReturnResponseEntityWithSubjectAvailable() {
        // Arrange
        String classId = "class123";
        List<SubjectResponse> subjectAvailable = Collections.singletonList(SubjectResponse.builder().build());
        when(scheduleService.getSubjectAvailable(classId)).thenReturn(subjectAvailable);

        // Act
        ResponseEntity<?> responseEntity = scheduleApiController.subjectAvailable(classId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subjectAvailable, responseEntity.getBody());
        verify(scheduleService, times(1)).getSubjectAvailable(classId);
    }

    @Test
    void scheduleDetail_shouldReturnResponseEntityWithScheduleDetail() {
        // Arrange
        String classId = "class123";
        String semesterId = "semester123";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(7);
        List<LessonResponse> scheduleDetail = Collections.singletonList(LessonResponse.builder().build());
        when(scheduleService.getScheduleDetail(classId, semesterId, start, end)).thenReturn(scheduleDetail);

        // Act
        ResponseEntity<?> responseEntity = scheduleApiController.scheduleDetail(classId, semesterId, start, end);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(scheduleDetail, responseEntity.getBody());
        verify(scheduleService, times(1)).getScheduleDetail(classId, semesterId, start, end);
    }
}