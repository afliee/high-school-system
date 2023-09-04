package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.DayConverter;
import com.highschool.highschoolsystem.dto.response.DayResponse;
import com.highschool.highschoolsystem.entity.DayEntity;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.repository.DayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DayServiceTest {
    @Mock
    private DayRepository dayRepository;

    private DayService dayService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dayService = new DayService(dayRepository);
    }

    @Test
    void getAllDays_ShouldReturnAllDays() {
        Collection<LessonEntity> lessonEntities = List.of(new LessonEntity());
        DayEntity dayEntity = new DayEntity("MONDAY", lessonEntities);
        DayEntity dayEntity1 = new DayEntity("THURSDAY", lessonEntities);
        when(dayRepository.findAll()).thenReturn(List.of(dayEntity, dayEntity1));
        MockedStatic<DayConverter> dayConverterMockedStatic = mockStatic(DayConverter.class);

        dayConverterMockedStatic.when(() -> DayConverter.toResponse(List.of(dayEntity, dayEntity1), new String[]{"id", "name"})).thenReturn(List.of(new DayResponse(), new DayResponse()));

        List<DayResponse> days = dayService.getAllDays();

        assertEquals(2, days.size());
        verify(dayRepository, times(1)).findAll();
    }
}
