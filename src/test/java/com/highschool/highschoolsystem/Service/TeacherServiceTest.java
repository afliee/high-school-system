package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacherService = new TeacherServiceImpl(teacherRepository, tokenRepository, userRepository, departmentRepository, subjectRepository, classRepository, lessonRepository, passwordEncoder, jwtService);
    }

    @Test
    void save_GivenValidObject_ShouldReturnTeacherResponse() {
        TeacherEntity teacherEntity = new TeacherEntity();

        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);

        TeacherEntity teacherEntity1 = teacherService.save(teacherEntity);

        assertEquals(teacherEntity, teacherEntity1);
        verify(teacherRepository, times(1)).save(teacherEntity);
    }

    @Test
    void update_GivenValidObject_ShouldReturnTeacherResponse() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        TeacherResponse teacherResponse = TeacherResponse.builder().build();

        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(Optional.of(teacherEntity));

        TeacherEntity teacherEntity1 = teacherService.updateById(teacherEntity.getId(), teacherResponse);

        verify(teacherRepository, times(1)).save(teacherEntity);
        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
    }

    @Test
    void findById_GivenValidId_ShouldReturnTeacherResponse() {
        TeacherEntity teacherEntity = new TeacherEntity();

        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(java.util.Optional.of(teacherEntity));

        TeacherEntity teacherEntity1 = teacherService.findById(teacherEntity.getId());

        assertEquals(teacherEntity, teacherEntity1);
        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
    }

    @Test
    void deleteById_GivenValidId_ShouldReturnTeacherResponse() {
        UserEntity userEntity =UserEntity.builder().build();
        SubjectEntity subjectEntity = SubjectEntity.builder().build();
        TeacherEntity teacherEntity = TeacherEntity.builder().subjects(List.of(subjectEntity)).build();
        ClassEntity classEntity = ClassEntity.builder().build();
        Collection<SubjectEntity> subjectEntities = Collections.singletonList(subjectEntity);

        when(teacherRepository.findById(userEntity.getId())).thenReturn(java.util.Optional.of(teacherEntity));
        when(userRepository.findByUserId(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(classRepository.findByChairmanId(teacherEntity.getId())).thenReturn(Optional.of(classEntity));

        teacherService.deleteById(userEntity.getId());

        verify(userRepository, times(1)).findByUserId(userEntity.getId());
        verify(teacherRepository, times(1)).deleteById(userEntity.getId());
        verify(classRepository, times(1)).findByChairmanId(teacherEntity.getId());
        verify(lessonRepository, times(1)).deleteAllBySubjectIdIn(teacherEntity.getSubjects().stream().map(BaseEntity::getId).toList());
        verify(subjectRepository, times(1)).deleteAllByTeacher(teacherEntity);
        verify(tokenRepository, times(1)).deleteByUserId(userEntity.getId());
        verify(userRepository, times(1)).deleteByUserId(userEntity.getId());
        verify(teacherRepository, times(1)).deleteById(userEntity.getId());
    }

    @Test
    void updateById_ShouldReturnTeacherResponse() {
        TeacherEntity teacherEntity = new TeacherEntity();
        TeacherResponse teacherResponse = new TeacherResponse();

        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(java.util.Optional.of(teacherEntity));

        teacherService.updateById(teacherEntity.getId(), teacherResponse);

        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
        verify(teacherRepository, times(1)).save(teacherEntity);
    }

    @Test
    void updateById_GivenInvalidId_ShouldReturnTeacherResponse() {
        TeacherEntity teacherEntity = new TeacherEntity();
        TeacherResponse teacherResponse = new TeacherResponse();

        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(java.util.Optional.of(teacherEntity));

        teacherService.updateById(teacherEntity.getId(), teacherResponse);

        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
        verify(teacherRepository, times(0)).save(teacherEntity);
    }

    @Test
    void findAllTeachers_ShouldReturnListOfTeacherResponse() {
        TeacherEntity teacherEntity = new TeacherEntity();
        Iterable<TeacherResponse> teacherResponses = Collections.singletonList(teacherEntity).stream().map(teacher -> TeacherResponse.builder().id(teacher.getId()).fullName(teacher.getFullName()).build()).toList();

        when(teacherRepository.findAll()).thenReturn(java.util.List.of(teacherEntity));

        when(teacherRepository.findAll()).thenReturn(java.util.List.of(teacherEntity));

        Iterable<TeacherResponse> teachers = teacherService.findAllTeachers();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(teacherResponses, teachers);
    }

    @Test
    void delete_GivenValidId_ShouldReturnTeacherResponse() {
        Collection<SubjectEntity> subjectEntities = Collections.singletonList(SubjectEntity.builder().build());
        UserEntity userEntity = UserEntity.builder().build();
        TeacherEntity teacherEntity = TeacherEntity.builder().subjects(subjectEntities).build();

        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(java.util.Optional.of(teacherEntity));
        when(userRepository.findByUserId(userEntity.getId())).thenReturn(Optional.of(userEntity));

        teacherService.deleteById(teacherEntity.getId());

        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
        verify(teacherRepository, times(1)).deleteById(teacherEntity.getId());
    }

    @Test
    void findByName_ShouldReturnTeachers() {
        TeacherEntity teacherEntity = new TeacherEntity();
        Iterable<TeacherResponse> teacherResponses = Collections.singletonList(teacherEntity).stream().map(teacher -> TeacherResponse.builder().id(teacher.getId()).fullName(teacher.getFullName()).build()).toList();

        when(teacherRepository.findByFullNameContainingIgnoreCase("")).thenReturn(java.util.List.of(teacherEntity));

        Iterable<TeacherResponse> teachers = teacherService.findByName("");

        verify(teacherRepository, times(1)).findByFullNameContainingIgnoreCase("");
        assertEquals(teacherResponses, teachers);
    }

    @Test
    void findByName_ShouldReturnTeacher() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();

        when(teacherRepository.findByName(teacherEntity.getId())).thenReturn(Optional.of(teacherEntity));

        Optional<TeacherEntity> teacherEntity1 = teacherRepository.findByName(teacherEntity.getName());

        verify(teacherRepository, times(1)).findByName(teacherEntity.getName());
    }

    @Test
    void findById_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> teacherService.findById(teacherEntity.getId()));
    }

    @Test
    void deleteById_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        when(userRepository.findById(teacherEntity.getId())).thenReturn(Optional.empty());

        teacherRepository.deleteById(teacherEntity.getId());

        assertThrows(UserNotFoundException.class, () -> teacherService.deleteById(teacherEntity.getId()));
    }

    @Test
    void deleteById_WhenTeacherNotFound_ShouldThrowUserNotFoundException() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(Optional.empty());

        teacherRepository.deleteById(teacherEntity.getId());

        assertThrows(UserNotFoundException.class, () -> teacherService.deleteById(teacherEntity.getId()));
        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
    }

    @Test
    void updateById_WhenTeacherNotFound_ShouldThrowUserNotFoundException() {
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        TeacherResponse teacherResponse = TeacherResponse.builder().build();

        when(teacherRepository.findById(teacherEntity.getId())).thenReturn(Optional.empty());

        teacherService.updateById(teacherEntity.getId(), teacherResponse);

        assertThrows(UserNotFoundException.class, () -> teacherService.updateById(teacherEntity.getId(), teacherResponse));
        verify(teacherRepository, times(1)).findById(teacherEntity.getId());
    }


}
