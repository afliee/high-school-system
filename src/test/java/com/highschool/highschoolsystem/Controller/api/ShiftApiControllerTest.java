package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.ShiftRequest;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.service.DayService;
import com.highschool.highschoolsystem.service.LessonService;
import com.highschool.highschoolsystem.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShiftApiControllerTest {

    @Mock
    private ShiftService shiftService;

    @Mock
    private LessonService lessonService;

    @Mock
    private DayService dayService;

    @InjectMocks
    private ShiftApiController shiftApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnResponseEntityWithShiftResponse() {
        // Arrange
        ShiftRequest shiftRequest = new ShiftRequest();
        ShiftResponse shiftResponse = new ShiftResponse();
        when(shiftService.create(shiftRequest)).thenReturn(shiftResponse);

        // Act
        ResponseEntity<ShiftResponse> responseEntity = shiftApiController.create(shiftRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(shiftResponse, responseEntity.getBody());
        verify(shiftService, times(1)).create(shiftRequest);
    }

    @Test
    void getAll_shouldReturnResponseEntityWithListOfShiftResponse() {
        // Arrange
        List<ShiftResponse> shiftResponses = Arrays.asList(new ShiftResponse(), new ShiftResponse());
        when(shiftService.getAll()).thenReturn(shiftResponses);

        // Act
        ResponseEntity<List<ShiftResponse>> responseEntity = shiftApiController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(shiftResponses, responseEntity.getBody());
        verify(shiftService, times(1)).getAll();
    }

    @Test
    void getAvailableTime_shouldReturnResponseEntityWithAvailableTime() {
        // Arrange
        String subjectId = "subject123";
        String semesterId = "semester123";
        Map<String, Boolean> availableTime = new HashMap<>();
        when(shiftService.getAvailableTime(subjectId, semesterId)).thenReturn(availableTime);

        // Act
        ResponseEntity<?> responseEntity = shiftApiController.getAvailableTime(subjectId, semesterId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(availableTime, responseEntity.getBody());
        verify(shiftService, times(1)).getAvailableTime(subjectId, semesterId);
    }
}