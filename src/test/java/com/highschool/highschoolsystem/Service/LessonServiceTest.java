package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.LessonConverter;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

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
}
