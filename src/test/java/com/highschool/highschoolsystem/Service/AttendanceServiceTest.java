package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.AttendanceConverter;
import com.highschool.highschoolsystem.dto.request.AttendanceRequest;
import com.highschool.highschoolsystem.dto.response.AttendanceResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.AttendanceRepository;
import com.highschool.highschoolsystem.repository.NavigatorRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AttendanceServiceTest {
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepositoyr;
    @Mock
    private NavigatorService navigatorService;
    @Mock
    private ClassService classService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private NavigatorRepository navigatorRepository;

    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        attendanceService = new AttendanceService(attendanceRepository, jwtService, userRepositoyr, navigatorService, classService, studentRepository, navigatorRepository);
    }

//    @Test
//    void getHistory_ShouldReturnMap() {
//        AttendanceEntity attendanceEntity = AttendanceEntity.builder().build();
//        MockedStatic<Collectors> collectorsMockedStatic = mockStatic(Collectors.class);
//
//        when(attendanceRepository.findAllByClassEntity_Id("1")).thenReturn(List.of(attendanceEntity));
//
//        Map<LocalDate, List<AttendanceEntity>> history = attendanceService.getHistory("1");
//
//        verify(attendanceRepository, times(1)).findAllByClassEntity_Id("1");
//    }

    @Test
    void getAttendance_ShouldReturnAttendanceResponse() {
        AttendanceEntity attendanceEntity = AttendanceEntity.builder().build();
        MockedStatic<AttendanceConverter> attendanceConverterMockedStatic = mockStatic(AttendanceConverter.class);

        attendanceConverterMockedStatic.when(() -> AttendanceConverter.toResponse(attendanceEntity)).thenReturn(new AttendanceResponse());
        when(attendanceRepository.findById("1")).thenReturn(java.util.Optional.of(attendanceEntity));
        AttendanceResponse attendanceResponse = attendanceService.getAttendance("1");

        verify(attendanceRepository, times(1)).findById("1");
    }

    @Test
    void testSubmit() {
        StudentEntity studentEntity = StudentEntity.builder().attendances(new ArrayList<>()).build();
        ClassEntity classEntity = ClassEntity.builder().build();
        NavigatorEntity navigator = NavigatorEntity.builder().student(studentEntity).build();
        AttendanceEntity attendanceEntity = AttendanceEntity.builder().build();
        AttendanceRequest attendanceRequest = AttendanceRequest.builder().studentIds(Collections.singletonList("1")).classId("1").build();

        when(studentRepository.findById("1")).thenReturn(Optional.of(studentEntity));
        when(classService.findById("1")).thenReturn(classEntity);

        attendanceService.submit(navigator, attendanceRequest);

        verify(attendanceRepository, times(1)).save(any(AttendanceEntity.class));
        verify(studentRepository, times(1)).save(studentEntity);
        verify(classService, times(1)).findById("1");
        verify(studentRepository, times(1)).findById("1");
    }

    @Test
    void update_WhenAttendanceNotFound_ShouldThrowsNotFoundException() {
        when(attendanceRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attendanceService.update("1", new AttendanceRequest()));
    }

    @Test
    void update_WhenStudentNotFound_ShouldThrowNotFoundException() {
        when(studentRepository.findById("1")).thenReturn(Optional.empty());
        when(attendanceRepository.findById("1")).thenReturn(Optional.of(AttendanceEntity.builder().build()));

        assertThrows(NotFoundException.class, () -> attendanceService.update("1", AttendanceRequest.builder().studentIds(Collections.singletonList("1")).build()));
    }

    @Test
    void submit_WhenStudentNotFound_ShouldThrowNotFoundException() {
        when(studentRepository.findById("1")).thenReturn(Optional.empty());
        when(classService.findById("1")).thenReturn(ClassEntity.builder().build());

        assertThrows(NotFoundException.class, () -> attendanceService.submit(NavigatorEntity.builder().build(), AttendanceRequest.builder().studentIds(Collections.singletonList("1")).build()));
    }

    @Test
    void getAttendance_WhenAttendanceNotFound_ShouldThrowNotFoundException() {
        when(attendanceRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attendanceService.getAttendance("1"));
    }
}
