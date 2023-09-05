package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.LessonConverter;
import com.highschool.highschoolsystem.dto.request.LessonRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LessonServiceTest {
    @Mock
    private DayRepository dayRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SemesterRepository semesterRepository;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private SemesterService semesterService;

    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lessonService = new LessonService(dayRepository, subjectRepository, semesterRepository, shiftRepository, lessonRepository, scheduleRepository, semesterService);
    }

    @Test
    void save_ShouldReturnLessonEntity() {
        LessonEntity lessonEntity = LessonEntity.builder().build();

        when(lessonRepository.save(lessonEntity)).thenReturn(lessonEntity);

        lessonService.save(lessonEntity);

        verify(lessonRepository).save(lessonEntity);
    }

    @Test
    void getLessonTimeAvailable_ShouldReturnLessonEntity() {
        LessonEntity lessonEntity = LessonEntity.builder().build();
        MockedStatic<LessonConverter> lessonConverterMockedStatic = mockStatic(LessonConverter.class);

        lessonConverterMockedStatic.when(() -> LessonConverter.toResponse(List.of(lessonEntity))).thenReturn(List.of(LessonResponse.builder().build()));
        when(lessonRepository.findAllBySubjectIdAndWeekSemesterId("1", "1")).thenReturn(List.of(lessonEntity));

        lessonService.getLessonTimeAvailable("1", "1");

        verify(lessonRepository).findAllBySubjectIdAndWeekSemesterId("1", "1");
    }

    @Test
    void create_WhenSemesterNotFound_ShouldThrownNotFoundException() {
        LessonRequest lessonRequest = LessonRequest.builder().build();
        when(semesterRepository.findById(lessonRequest.getSemesterId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> lessonService.create(lessonRequest));
    }

    @Test
    void getLessonToday_ShouldReturnListOfLessonEntity() {
        LessonEntity lessonEntity = LessonEntity.builder().build();
        SemesterEntity semesterEntity = SemesterEntity.builder().build();

        when(semesterService.findCurrentSemesterEntity()).thenReturn(semesterEntity);

        lessonService.getLessonToday(10);

        verify(semesterService, times(1)).findCurrentSemesterEntity();
        verify(lessonRepository, times(1)).findAllByWeekSemesterIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(semesterEntity.getId(), LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2));
    }
}
