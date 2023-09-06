package com.highschool.highschoolsystem.service;
import com.highschool.highschoolsystem.converter.SubjectConverter;
import com.highschool.highschoolsystem.dto.request.SubjectRequest;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import javax.security.auth.Subject;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private AssignmentService assignmentService;

    private SubjectService subjectService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subjectService = new SubjectService(subjectRepository, teacherRepository, departmentRepository, levelRepository, lessonRepository, scheduleRepository, assignmentService);
    }

    @Test
    void create_ShouldReturnSubjectResponse() {
        SubjectRequest subjectRequest = SubjectRequest.builder().build();
        TeacherEntity teacherEntity = new TeacherEntity();
        Collection<SubjectEntity> subjectEntities = new ArrayList<>();
        LevelEntity levelEntity = LevelEntity.builder().subjects(subjectEntities).build();
        DepartmentEntity departmentEntity = DepartmentEntity.builder().subjects(subjectEntities).build();
        SubjectEntity subjectEntity = SubjectEntity.builder().build();
        MockedStatic<SubjectConverter> subjectConverterMockedStatic = mockStatic(SubjectConverter.class);

        when(levelRepository.findById(subjectRequest.getLevelId())).thenReturn(Optional.of(levelEntity));
        when(teacherRepository.findById(subjectRequest.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(departmentRepository.findById(subjectRequest.getDepartmentId())).thenReturn(Optional.of(departmentEntity));
        when(subjectRepository.save(subjectEntity)).thenReturn(subjectEntity);
        subjectConverterMockedStatic.when(() -> SubjectConverter.toEntity(subjectRequest, teacherEntity, departmentEntity, levelEntity)).thenReturn(subjectEntity);


        SubjectResponse subjectResponse = subjectService.create(subjectRequest);

        verify(subjectRepository, times(1)).save(subjectEntity);
        verify(levelRepository, times(1)).findById(subjectRequest.getLevelId());
        verify(teacherRepository, times(1)).findById(subjectRequest.getTeacherId());
        verify(departmentRepository, times(1)).findById(subjectRequest.getDepartmentId());
    }

    @Test
    void update_ShouldReturnSubjectResponse() {
        SubjectRequest subjectRequest = new SubjectRequest();
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        teacherEntity.setCreatedDate(LocalDate.now());
        DepartmentEntity departmentEntity = new DepartmentEntity();
        SubjectEntity subjectEntity = SubjectConverter.toEntity(subjectRequest, teacherEntity, departmentEntity, LevelEntity.builder().build());

        when(teacherRepository.findById(subjectRequest.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(departmentRepository.findById(subjectRequest.getDepartmentId())).thenReturn(Optional.of(departmentEntity));
        when(subjectRepository.findById(subjectEntity.getId())).thenReturn(Optional.of(subjectEntity));
        when(subjectRepository.save(subjectEntity)).thenReturn(subjectEntity);

        SubjectResponse subjectResponse = subjectService.update(subjectEntity.getId(), subjectRequest);

        verify(subjectRepository, times(1)).save(subjectEntity);
        verify(teacherRepository, times(1)).findById(subjectRequest.getTeacherId());
        verify(departmentRepository, times(1)).findById(subjectRequest.getDepartmentId());
    }

    @Test
    void findById_ShouldReturnSubjectResponse() {
        SubjectEntity subjectEntity = SubjectEntity.builder().build();
        SubjectResponse subjectResponse = SubjectResponse.builder().build();
        MockedStatic<SubjectConverter> subjectConverterMockedStatic = mockStatic(SubjectConverter.class);

        subjectConverterMockedStatic.when(() -> SubjectConverter.toResponse(subjectEntity)).thenReturn(subjectResponse);

        when(subjectRepository.findById(subjectEntity.getId())).thenReturn(Optional.of(subjectEntity));

        SubjectResponse s = subjectService.findById(subjectEntity.getId());

        verify(subjectRepository, times(1)).findById(subjectEntity.getId());
    }

    @Test
    void findByIdEntity() {
        SubjectEntity subjectEntity = SubjectEntity.builder().build();

        when(subjectRepository.findById(subjectEntity.getId())).thenReturn(Optional.of(subjectEntity));

        Optional<SubjectEntity> subjectEntity1 = subjectService.findByIdEntity(subjectEntity.getId());

        verify(subjectRepository, times(1)).findById(subjectEntity.getId());
    }

    @Test
    void testDeleteSubject() {
        SubjectEntity subjectEntity = SubjectEntity.builder().build();
        DepartmentEntity departmentEntity = DepartmentEntity.builder().subjects(new ArrayList<>()).build();
        MockedStatic<SubjectConverter> subjectConverterMockedStatic = mockStatic(SubjectConverter.class);
        subjectConverterMockedStatic.when(() -> SubjectConverter.toResponse(subjectEntity)).thenReturn(new SubjectResponse());
        subjectEntity.setDepartment(departmentEntity);

        when(subjectRepository.findById(subjectEntity.getId())).thenReturn(Optional.of(subjectEntity));
        when(departmentRepository.findById(subjectEntity.getDepartment().getId())).thenReturn(Optional.of(departmentEntity));

        subjectService.delete(subjectEntity.getId());

        verify(subjectRepository, times(1)).findById(subjectEntity.getId());
        verify(departmentRepository, times(1)).findById(subjectEntity.getDepartment().getId());
    }

    @Test
    void testGet() {
        SubjectEntity subjectEntity = SubjectEntity.builder().build();
        MockedStatic<SubjectConverter> subjectConverterMockedStatic = mockStatic(SubjectConverter.class);

        subjectConverterMockedStatic.when(() -> SubjectConverter.toResponse(subjectEntity)).thenReturn(new SubjectResponse());
        when(subjectRepository.findById(subjectEntity.getId())).thenReturn(Optional.of(subjectEntity));

        subjectService.get(subjectEntity.getId());

        verify(subjectRepository, times(1)).findById(subjectEntity.getId());
    }

    @Test
    void findAllByIdIn_ShouldReturnListOfSubject() {
        List<SubjectEntity> subjectEntities = Collections.singletonList(SubjectEntity.builder().build());

        when(subjectRepository.findAllByIdIn(Collections.singletonList("1"))).thenReturn(subjectEntities);
        subjectService.findAllByIdIn(Collections.singletonList("1"));

        verify(subjectRepository, times(1)).findAllByIdIn(Collections.singletonList("1"));
    }

    @Test
    void create_WhenLevelNotFound_ShouldThrowNotFoundException() {
        SubjectRequest subjectRequest = SubjectRequest.builder().levelId("1").build();

        when(levelRepository.findById(subjectRequest.getLevelId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.create(subjectRequest));
    }

    @Test
    void create_WhenTeacherNotFound_ShouldThrowNotFoundException() {
        SubjectRequest subjectRequest = SubjectRequest.builder().levelId("1").teacherId("1").build();

        when(levelRepository.findById(subjectRequest.getLevelId())).thenReturn(Optional.of(LevelEntity.builder().build()));
        when(teacherRepository.findById(subjectRequest.getTeacherId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.create(subjectRequest));
    }

    @Test
    void create_WhenDepartmentNotFound_ShouldThrowNotFoundException() {
        SubjectRequest subjectRequest = SubjectRequest.builder().levelId("1").teacherId("1").departmentId("1").build();

        when(levelRepository.findById(subjectRequest.getLevelId())).thenReturn(Optional.of(LevelEntity.builder().build()));
        when(teacherRepository.findById(subjectRequest.getTeacherId())).thenReturn(Optional.of(TeacherEntity.builder().build()));
        when(departmentRepository.findById(subjectRequest.getDepartmentId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.create(subjectRequest));
    }

    @Test
    void findById_SubjectNotFound_ShouldThrowNotFoundException() {
        when(subjectRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.findById("1"));
    }

    @Test
    void delete_WhenSubjectNotFound_ShouldThrowNotFoundException() {
        when(subjectRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.delete("1"));
    }

    @Test
    void get_WhenSubjectNotFound_ShouldThrowNotFoundException() {
        when(subjectRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.get("1"));
    }

    @Test
    void getStudents_WhenSubjectNotFound_ShouldThrowNotFoundException() {
        when(subjectRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subjectService.getStudents("1"));
    }
}
