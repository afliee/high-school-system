package com.highschool.highschoolsystem.Service;

import com.highschool.highschoolsystem.dto.request.AddClassRequest;
import com.highschool.highschoolsystem.dto.response.ClassResponse;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.repository.*;
import com.highschool.highschoolsystem.service.ClassService;
import com.highschool.highschoolsystem.service.LessonService;
import com.highschool.highschoolsystem.service.SubmittingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClassServiceTest {
    @Mock
    private ClassRepository classRepository;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private LessonService lessonService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock AddClassRequest addClassRequest;

    private ClassService classService;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private SubmittingService submittingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classService = new ClassService(classRepository, levelRepository, studentRepository, semesterRepository, userRepository, passwordEncoder, tokenRepository, teacherRepository, scheduleRepository, attendanceRepository, submittingService);
    }

    @Test
    void save_ShouldReturnClassResponse() {
        LevelEntity levelEntity = new LevelEntity();
        SemesterEntity semesterEntity = new SemesterEntity();
        TeacherEntity teacherEntity = new TeacherEntity();
        ClassResponse classResponse = new ClassResponse();
        when(levelRepository.findById(levelEntity.getId())).thenReturn(java.util.Optional.of(levelEntity));
        when(semesterRepository.findById(semesterEntity.getId())).thenReturn(java.util.Optional.of(semesterEntity));
        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(java.util.Optional.of(teacherEntity));
        when(classRepository.save(any())).thenReturn(classResponse);
//        when(addClassRequest.getStudents()).thenReturn(new MultipartFile());

        ClassResponse classResponse1 = classService.save(addClassRequest);

        assertEquals(classResponse, classResponse1);
        verify(classRepository, times(1)).save(any());
        verify(levelRepository, times(1)).findById(levelEntity.getId());
        verify(semesterRepository, times(1)).findById(semesterEntity.getId());
        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
        verify(classRepository, times(1)).save(any());
    }
}
