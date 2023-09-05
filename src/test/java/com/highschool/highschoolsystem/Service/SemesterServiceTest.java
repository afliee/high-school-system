package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SemesterConverter;
import com.highschool.highschoolsystem.dto.request.SemesterRequest;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.SemesterRepository;
import com.highschool.highschoolsystem.repository.SubjectRepository;
import com.highschool.highschoolsystem.repository.WeekRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SemesterServiceTest {
    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private WeekRepository weekRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private ClassService classService;

    private SemesterService semesterService;

    @Mock
    private SubjectRepository subjectRepository;
    MockedStatic<SemesterConverter> semesterConverterMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        semesterConverterMockedStatic = mockStatic(SemesterConverter.class);
        semesterService = new SemesterService(semesterRepository, weekRepository, classRepository, classService, subjectRepository);
    }
    @AfterEach
    void tearDown() {
        semesterConverterMockedStatic.close();
    }

    @Test
    void save_ValidAddSemesterRequest_ShouldReturnTheSemesterResponse() {
        SemesterEntity semesterEntity = SemesterEntity.builder().startDate(LocalDate.now()).endDate(LocalDate.now()).build();
        ArrayList<WeekEntity> weekEntities = new ArrayList<>();
        weekEntities.add(WeekEntity.builder()
                        .name("Week 1")
                        .startDate(semesterEntity.getStartDate())
                        .endDate(semesterEntity.getStartDate().plusDays(6))
                        .weekIndex(1)
                        .semester(semesterEntity)
                        .build());

        semesterConverterMockedStatic.when(() -> SemesterConverter.toResponse(semesterEntity)).thenReturn(new SemesterResponse());
        when(semesterRepository.save(semesterEntity)).thenReturn(semesterEntity);
        when(semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(semesterEntity.getStartDate(), semesterEntity.getEndDate())).thenReturn(Optional.empty());
        when(weekRepository.saveAll(weekEntities)).thenReturn(weekEntities);

        SemesterResponse semesterResponse = semesterService.save(semesterEntity);

        verify(semesterRepository, times(1)).findByStartDateLessThanEqualAndEndDateGreaterThanEqual(semesterEntity.getStartDate(), semesterEntity.getEndDate());
        verify(semesterRepository, times(1)).save(semesterEntity);
        verify(weekRepository, times(1)).saveAll(weekEntities);
        assertEquals(semesterEntity.getId(), semesterResponse.getId());
    }

    @Test
    void findAll_ShouldReturnAllSemester() {
        SemesterEntity semesterEntity = new SemesterEntity();
        semesterConverterMockedStatic.when(() -> SemesterConverter.toResponse(semesterEntity)).thenReturn(new SemesterResponse());

        when(semesterRepository.findAllByOrderByStartDateDesc()).thenReturn(List.of(semesterEntity));

        List<SemesterResponse> semesters = semesterService.findAll();

        verify(semesterRepository, times(1)).findAllByOrderByStartDateDesc();
    }

    @Test
    void findById_ShouldReturnSemesterResponse() {
        SemesterEntity semesterEntity = SemesterEntity.builder().build();
        semesterEntity.setId("1");
        semesterConverterMockedStatic.when(() -> SemesterConverter.toResponse(semesterEntity)).thenReturn(SemesterResponse.builder().id("1").build());

        when(semesterRepository.findById(semesterEntity.getId())).thenReturn(java.util.Optional.of(semesterEntity));

        SemesterResponse semesterResponse = semesterService.findById(semesterEntity.getId());

        assertNotNull(semesterResponse);
        assertEquals(semesterEntity.getId(), semesterResponse.getId());
        verify(semesterRepository, times(1)).findById(semesterEntity.getId());
    }

    @Test
    void testDeleteSemester() {
        Collection<ClassEntity> classEntityCollections = Collections.singleton(ClassEntity.builder().build());
        Set<SubjectEntity> subjectEntityCollections = Collections.singleton(SubjectEntity.builder().build());
        Collection<ScheduleEntity> scheduleEntities = Collections.singleton(ScheduleEntity.builder().subjects(subjectEntityCollections).build());
        SemesterEntity semesterEntity = SemesterEntity.builder().classes(classEntityCollections).schedules(scheduleEntities).build();

        when(semesterRepository.findById("1")).thenReturn(Optional.of(semesterEntity));

        semesterService.delete("1");

        verify(semesterRepository, times(1)).findById("1");
        verify(semesterRepository, times(1)).delete(semesterEntity);
    }

    @Test
    void findByIdEntity_ShouldReturnSemeseterEntity() {
        SemesterEntity semesterEntity = SemesterEntity.builder().build();

        when(semesterRepository.findById("1")).thenReturn(Optional.of(semesterEntity));

        SemesterEntity semesterEntity1 = semesterService.findByIdEntity("1");

        verify(semesterRepository, times(1)).findById("1");
    }

    @Test
    void update_ShouldReturnSemesterResposne() {
        SemesterEntity semesterEntity = SemesterEntity.builder().build();
        SemesterRequest semesterRequest = SemesterRequest.builder().build();
        semesterConverterMockedStatic.when(() -> SemesterConverter.toResponse(semesterEntity)).thenReturn(new SemesterResponse());
        when(semesterRepository.save(semesterEntity)).thenReturn(semesterEntity);
        when(semesterRepository.findById(semesterEntity.getId())).thenReturn(Optional.of(semesterEntity));

        SemesterResponse semesterResponse = semesterService.update(semesterEntity.getId(), semesterRequest);

        verify(semesterRepository, times(1)).findById(semesterEntity.getId());
        verify(semesterRepository, times(1)).save(semesterEntity);
    }

    @Test
    void testSave_SemesterAlreadyExists_ThrowsRuntimeException() {
        // Mock the behavior of the semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual() method
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 6, 30);
        SemesterEntity existingSemester = new SemesterEntity();
        existingSemester.setStartDate(startDate);
        existingSemester.setEndDate(endDate);

        when(semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDate, startDate)).thenReturn(Optional.of(existingSemester));

        // Create a new semester request
        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setStartDate(startDate);
        semesterEntity.setEndDate(endDate);

        // Call the save() method of the semesterService and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> semesterService.save(semesterEntity));
    }

    @Test
    void testFindCurrentSemester_NoSemesterFound_ThrowsNotFoundException() {
        // Mock the behavior of the semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual() method
        LocalDate currentDay = LocalDate.now();

        when(semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(currentDay, currentDay)).thenReturn(Optional.empty());

        // Call the findCurrentSemester() method of the semesterService and verify that it throws a NotFoundException
        assertThrows(NotFoundException.class, () -> semesterService.findCurrentSemester());
    }

    @Test
    void testFindById_SemesterNotFound_ThrowsNotFoundException() {
        // Mock the behavior of the semesterRepository.findById() method
        String semesterId = "1";

        when(semesterRepository.findById(semesterId)).thenReturn(Optional.empty());

        // Call the findById() method of the semesterService and verify that it throws a NotFoundException
        assertThrows(NotFoundException.class, () -> semesterService.findById(semesterId));
    }

    @Test
    void testUpdate_SemesterNotFound_ThrowsNotFoundException() {
        // Mock the behavior of the semesterRepository.findById() method
        String semesterId = "1";
        SemesterRequest request = new SemesterRequest();
        request.setName("New Semester");
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 6, 30));

        when(semesterRepository.findById(semesterId)).thenReturn(Optional.empty());

        // Call the update() method of the semesterService and verify that it throws a NotFoundException
        assertThrows(NotFoundException.class, () -> semesterService.update(semesterId, request));
    }

    @Test
    void testDelete_SemesterNotFound_ThrowsNotFoundException() {
        // Mock the behavior of the semesterRepository.findById() method
        String semesterId = "1";

        when(semesterRepository.findById(semesterId)).thenReturn(Optional.empty());

        // Call the delete() method of the semesterService and verify that it throws a NotFoundException
        assertThrows(NotFoundException.class, () -> semesterService.delete(semesterId));
    }

}

