package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.response.DayResponse;
import com.highschool.highschoolsystem.service.DayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class DayApiControllerTest {

    @Mock
    private DayService dayService;

    @InjectMocks
    private DayApiController dayApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDays_shouldReturnListOfDayResponses() {
        List<DayResponse> expectedDays = new ArrayList<>();
        expectedDays.add(DayResponse.builder().name("Monday").build());
        expectedDays.add(DayResponse.builder().name("Tuesday").build());

        when(dayService.getAllDays()).thenReturn(expectedDays);

        List<DayResponse> actualDays = dayApiController.getAllDays();

        assert actualDays.equals(expectedDays);
        verify(dayService).getAllDays();
    }
}