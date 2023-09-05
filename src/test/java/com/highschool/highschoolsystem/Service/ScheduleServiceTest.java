package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SubjectConverter;
import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.LessonRepository;
import com.highschool.highschoolsystem.repository.LevelRepository;
import com.highschool.highschoolsystem.repository.ScheduleRepository;
import com.highschool.highschoolsystem.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.nio.channels.OverlappingFileLockException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ScheduleServiceTest {
    @Mock
    private LessonService lessonService;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SemesterService semesterService;

    @Mock
    private ClassService classService;
    @Mock
    private LevelRepository levelRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private SubjectService subjectService;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduleService = new ScheduleService(lessonService, scheduleRepository, subjectRepository, semesterService, classService, levelRepository, lessonRepository, subjectService);
    }

    @Test
    void getSubjectGroupByLevel_ShouldReturnSubjectGroupByLevel() {
        List<SubjectEntity> subjectEntities = Collections.singletonList(SubjectEntity.builder().build());
        SubjectEntity subjectEntity = SubjectEntity.builder().build();
        LevelEntity levelEntity = LevelEntity.builder().subjects(subjectEntities).build();
        MockedStatic<SubjectConverter> subjectConverterMockedStatic = mockStatic(SubjectConverter.class);
        subjectConverterMockedStatic.when(() -> SubjectConverter.toResponse(subjectEntities)).thenReturn(Collections.singletonList(SubjectResponse.builder().departmentDetail(new DepartmentResponse())));
        when(levelRepository.findById(levelEntity.getId())).thenReturn(Optional.of(levelEntity));

        scheduleService.getSubjectGroupedByLevel(levelEntity.getId());

        verify(levelRepository, times(1)).findById(levelEntity.getId());
    }

    @Test
    void create_ShouldReturnCreatedSchedule() {
        SchedulingRequest schedulingRequest = new SchedulingRequest();

        List<SubjectEntity> subjectEntityList = Collections.singletonList(SubjectEntity.builder().build());
        Set<LessonEntity> lessonEntities = Collections.singleton(LessonEntity.builder().build());
        SemesterEntity semesterEntity = SemesterEntity.builder().build();
        ClassEntity classEntity = ClassEntity.builder().build();
        ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                .isExpired(false)
                .subjects(new HashSet<>(subjectEntityList))
                .expiredDate(semesterEntity.getEndDate())
                .semester(semesterEntity)
                .classEntity(classEntity)
                .build();

        when(subjectService.findAllByIdIn(schedulingRequest.getSubjectIds())).thenReturn(subjectEntityList);
        when(lessonService.findAllByIdIn(schedulingRequest.getLessonIds())).thenReturn(lessonEntities);
        when(semesterService.findByIdEntity(schedulingRequest.getSemesterId())).thenReturn(semesterEntity);
        when(classService.findById(schedulingRequest.getClassId())).thenReturn(classEntity);
        when(subjectRepository.findAllById(schedulingRequest.getSubjectIds())).thenReturn(subjectEntityList);

        scheduleService.create(schedulingRequest);

        verify(scheduleRepository, times(1)).save(scheduleEntity);
        verify(subjectRepository, times(1)).saveAll(subjectEntityList);
        verify(lessonService, times(1)).findAllByIdIn(schedulingRequest.getLessonIds());
        verify(subjectService, times(1)).findAllByIdIn(schedulingRequest.getSubjectIds());
        verify(semesterService, times(1)).findByIdEntity(schedulingRequest.getSemesterId());
        verify(classService, times(1)).findById(schedulingRequest.getClassId());
        verify(subjectRepository, times(1)).findAllById(schedulingRequest.getSubjectIds());
        verify(scheduleRepository, times(1)).save(scheduleEntity);
        verify(subjectRepository, times(1)).saveAll(subjectEntityList);
    }

    @Test
    void testDelete() {
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().build();
        List<SubjectEntity> subjectEntities = Collections.singletonList(SubjectEntity.builder().build());

        when(scheduleRepository.findById(scheduleEntity.getId())).thenReturn(Optional.of(scheduleEntity));
        when(subjectRepository.findAllById(scheduleEntity.getSubjects().stream().map(SubjectEntity::getId).toList())).thenReturn(subjectEntities);

        scheduleService.delete(scheduleEntity.getId());

        verify(scheduleRepository, times(1)).findById(scheduleEntity.getId());
        verify(subjectRepository, times(1)).findAllById(scheduleEntity.getSubjects().stream().map(SubjectEntity::getId).toList());
    }

    @Test
    void testRevoke() {
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().build();
        when(scheduleRepository.findById(scheduleEntity.getId())).thenReturn(Optional.of(scheduleEntity));

        scheduleService.revoke(scheduleEntity.getId());

        verify(scheduleRepository, times(1)).findById(scheduleEntity.getId());
    }

    @Test
    void getSubjectGroupByLevel_WhenLevelNotFound() {
        when(levelRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> scheduleService.getSubjectGroupedByLevel("1"));
    }

    @Test
    void getSubjectAvaible_WhenScheduleNotFound_ShouldThrowNotFoundException() {
        when(scheduleRepository.findAllByClassEntity_Id("1")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> scheduleService.getSubjectAvailable("1"));
    }
    
    @Test
    void getScheduleDetail() {
        when(scheduleRepository.findAllByClassEntity_IdAndSemester_Id("1", "1")).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> scheduleService.getScheduleDetail("1", "1", LocalDate.now(), LocalDate.now()));
    }

    @Test
    void revoke_WhenScheduleNotFound_ShouldThrowNotFoundException() {
        when(scheduleRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> scheduleService.revoke("1"));
    }


}
