package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.LessonRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;

class LessonApiControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonApiController lessonApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_withValidRequest_shouldReturnCreatedResponse() {
        LessonRequest request = new LessonRequest();
        request.setDayIds(List.of("day1", "day2"));
        request.setShiftIds(List.of("shift1", "shift2"));

        when(lessonService.create(request)).thenReturn(List.of(LessonResponse.builder().build()));

        ResponseEntity<?> response = lessonApiController.create(request);

        assert response.getStatusCode() == HttpStatus.OK;
        assert Objects.equals(response.getBody(), List.of(LessonResponse.builder().build()));
        verify(lessonService).create(request);
    }

    @Test
    void create_withMismatchedDayAndShiftSizes_shouldReturnBadRequestResponse() {
        LessonRequest request = new LessonRequest();
        request.setDayIds(List.of("day1", "day2"));
        request.setShiftIds(List.of("shift1"));

        ResponseEntity<?> response = lessonApiController.create(request);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody().equals("Day and shift must be the same size");
        verify(lessonService, never()).create(request);
    }

    @Test
    void get_withSemesterIdAndSubjectId_shouldReturnListOfLessons() {
        String semesterId = "semester1";
        String subjectId = "subject1";

        when(lessonService.get(semesterId, subjectId)).thenReturn(List.of());

        List<?> response = lessonApiController.get(semesterId, subjectId);

        verify(lessonService).get(semesterId, subjectId);
    }

    @Test
    void get_withSubjectIdSemesterIdStartEndAndIsDetail_shouldReturnListOfLessons() {
        String subjectId = "subject1";
        String semesterId = "semester1";
        LocalDate start = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2022, 1, 31);
        boolean isDetail = true;
        List<String> lessons = List.of("lesson1", "lesson2");

        when(lessonService.get(subjectId, semesterId, start, end, isDetail)).thenReturn(List.of());

        List<?> response = lessonApiController.get(subjectId, semesterId, start, end, isDetail);

        verify(lessonService).get(subjectId, semesterId, start, end, isDetail);
    }
}