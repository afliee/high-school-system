package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.ClassConverter;
import com.highschool.highschoolsystem.dto.request.AddClassRequest;
import com.highschool.highschoolsystem.dto.response.ClassResponse;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import com.highschool.highschoolsystem.service.ClassService;
import com.highschool.highschoolsystem.service.LessonService;
import com.highschool.highschoolsystem.service.ScheduleService;
import com.highschool.highschoolsystem.service.SubmittingService;
import ognl.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

//    @Mock
//    private ScheduleRepository scheduleRepository;
    @Mock
    private ScheduleService scheduleService;

    @Mock AddClassRequest addClassRequest;

    private ClassService classService;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private SubmittingService submittingService;
    @Mock
    private FaultRepository faultRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classService = new ClassService(classRepository, levelRepository, studentRepository, semesterRepository, userRepository, passwordEncoder, tokenRepository, teacherRepository, scheduleService, attendanceRepository, submittingService, faultRepository);
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

    @Test
    void findByTeacher_ShouldReturnClass() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        ClassEntity classEntity = ClassEntity.builder().chairman(teacherEntity).build();

        when(classRepository.findByChairmanId(classEntity.getChairman().getId())).thenReturn(Optional.of(classEntity));

        ClassEntity classEntity1 = classService.findByTeacher(classEntity.getChairman().getId());

        verify(classRepository, times(1)).findByChairmanId(classEntity.getChairman().getId());
    }

    @Test
    void findById_ShouldReturnClassEntity() {
        ClassEntity classEntity = ClassEntity.builder().build();

        when(classRepository.findById("1")).thenReturn(Optional.of(classEntity));

        ClassEntity classEntity1 = classService.findById("1");

        verify(classRepository, times(1)).findById("1");
    }

    @Test
    void testSetChairman() {
        ClassEntity classEntity = ClassEntity.builder().build();
        TeacherEntity teacherEntity = TeacherEntity.builder().build();

        when(classRepository.findById("1")).thenReturn(Optional.of(classEntity));
        when(teacherRepository.findById("1")).thenReturn(Optional.of(teacherEntity));
        when(classRepository.save(classEntity)).thenReturn(classEntity);

        classService.setChairman("1", "1");

        verify(classRepository, times(1)).findById("1");
        verify(teacherRepository, times(1)).findById("1");
        verify(classRepository, times(1)).save(classEntity);
    }

    @Test
    void testDeleteClass() {
        UserEntity userEntity = UserEntity.builder().build();
        StudentEntity studentEntity = StudentEntity.builder().build();
        Collection<StudentEntity> studentEntities = List.of(studentEntity);
        ClassEntity classEntity = ClassEntity.builder().students(studentEntities).build();

        when(classRepository.findById("1")).thenReturn(Optional.of(classEntity));
        when(userRepository.findByUserId(studentEntity.getId())).thenReturn(Optional.of(userEntity));

        classService.delete("1");

        verify(classRepository, times(1)).findById("1");
        verify(userRepository, times(1)).findByUserId(studentEntity.getId());
        verify(classRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteStudentInClass() {
        TokenEntity tokenEntity = TokenEntity.builder().build();
        Collection<StudentEntity> studentEntities = new ArrayList<>();
        ClassEntity classEntity = ClassEntity.builder().students(studentEntities).build();
        StudentEntity studentEntity = StudentEntity.builder().build();
        UserEntity userEntity = UserEntity.builder().build();

        when(classRepository.findById(classEntity.getId())).thenReturn(Optional.of(classEntity));
        when(studentRepository.findById(studentEntity.getId())).thenReturn(Optional.of(studentEntity));
        when(userRepository.findByUserId(studentEntity.getId())).thenReturn(Optional.ofNullable(userEntity));

        classService.delete(classEntity.getId(), studentEntity.getId());

        verify(classRepository, times(1)).findById(classEntity.getId());
        verify(studentRepository, times(1)).findById(studentEntity.getId());
        verify(userRepository, times(1)).findByUserId(studentEntity.getId());
    }

    @Test
    void get_ShouldReturnClassResponse() {
        MockedStatic<ClassConverter> classConverterMockedStatic = mockStatic(ClassConverter.class);
        List<StudentEntity> studentEntities = Collections.singletonList(StudentEntity.builder().build());
        ClassEntity classEntity = ClassEntity.builder().students(studentEntities).build();
        classConverterMockedStatic.when(() -> ClassConverter.toResponse(classEntity)).thenReturn(new ClassResponse());

        when(classRepository.findById(classEntity.getId())).thenReturn(Optional.of(classEntity));

        ClassResponse classResponse = classService.get(classEntity.getId(), 1, 1);

        verify(classRepository, times(1)).findById(classEntity.getId());
    }

    @Test
    void get_WhenClassNotFound_ShouldThrowNotFoundException() {
        when(classRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.get("1", 1, 1));
    }

    @Test
    void delete_WhenUserNotFound_ShouldThorwNotFoundException() {
        when(userRepository.findByUserId("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.delete("1", "1"));
    }

    @Test
    void delete_WhenClassNotFound_ShouldThrowNotFoundException() {
        when(classRepository.findById("1")).thenReturn(Optional.empty());
        when(userRepository.findByUserId("1")).thenReturn(Optional.of(UserEntity.builder().build()));

        assertThrows(NotFoundException.class, () -> classService.delete("1", "1"));
    }

    @Test
    void deleteClass_WhenClassNotFound_ShouldThrowNotFoundException() {
        when(classRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.delete("1"));
    }

    @Test
    void deleteClass_WhenStudentNotFound_ShouldThrowNotFoundException() {
        when(classRepository.findById("1")).thenReturn(Optional.of(ClassEntity.builder().students(Collections.singletonList(StudentEntity.builder().build())).build()));
        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.delete("1"));
    }

    @Test
    void setChairMan_WhenClassNotFound_ShouldThrowNotFoundException() {
        when(classRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.setChairman("1", "1"));
    }

    @Test
    void setChairMan_WhenTeacherNotFound() {
        ClassEntity classEntity = ClassEntity.builder().build();

        when(classRepository.findById("1")).thenReturn(Optional.of(classEntity));
        when(teacherRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.setChairman("1", "1"));
    }

    @Test
    void findById_WhenClassNotFound_ShouldThrowNotFoundException() {
        when(classRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classService.findById("1"));
    }

}

