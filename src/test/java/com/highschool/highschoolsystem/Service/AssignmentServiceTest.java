package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.SubmitStatus;
import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.dto.request.GradingRequest;
import com.highschool.highschoolsystem.dto.request.SubmitRequest;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.AssignmentRepository;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import com.highschool.highschoolsystem.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AssignmentServiceTest {
    @Mock
    private TeacherServiceImpl teacherService;
    @Mock
    private ClassService classService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private ClassRepository classRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SubmittingRepository submittingRepository;
    @Mock
    private MultipartFile attachment;
    @Mock
    private EmailService emailService;

    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assignmentService = new AssignmentService(teacherService, classService, subjectService, assignmentRepository, classRepository, studentRepository, submittingRepository, emailService);
    }

    @Test
    void createAssignmentTest() {
        CreateAssignmentRequest createAssignmentRequest = CreateAssignmentRequest.builder()
                .isDue("true")
                .points("10")
                .startedDate("2018-12-29 19:34:50")
                .closedDate("2018-12-29 19:34:50")
                .attachment(attachment)
                .build();
        TeacherEntity teacherEntity = TeacherEntity.builder().build();
        SubjectEntity subjectEntity = SubjectEntity.builder().build();

        when(teacherService.findById(createAssignmentRequest.getTeacherId())).thenReturn(teacherEntity);
        when(subjectService.findByIdEntity(createAssignmentRequest.getSubjectId())).thenReturn(java.util.Optional.of(subjectEntity));
        when(attachment.getOriginalFilename()).thenReturn("test.pdf");

        assignmentService.create(createAssignmentRequest);

        verify(teacherService, times(1)).findById("1");
        verify(subjectService, times(1)).findByIdEntity("1");
    }

    @Test
    void grading_ShouldReturnSubmittingResponse() {
        GradingRequest gradingRequest = new GradingRequest();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder().points(10).build();
        Submitting submitting = Submitting.builder().status(SubmitStatus.SUBMITTED).score(8).assignment(assignmentEntity).build();
        MockedStatic<SubmittingConverter> submittingConverterMockedStatic = mockStatic(SubmittingConverter.class);
        submittingConverterMockedStatic.when(() -> SubmittingConverter.toResponse(submitting)).thenReturn(new SubmittingResponse());

        when(submittingRepository.findById(submitting.getId())).thenReturn(Optional.of(submitting));

        assignmentService.grading(submitting.getId(), gradingRequest);

        verify(submittingRepository, times(1)).findById(submitting.getId());
        verify(submittingRepository, times(1)).save(submitting);
    }

    @Test
    void testDelete() {
        Set<Submitting> submittingSet = new HashSet<>();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder().submitting(submittingSet).build();

        when(assignmentRepository.findById(assignmentEntity.getId())).thenReturn(Optional.of((assignmentEntity)));

        assignmentService.delete(assignmentEntity.getId());

        verify(assignmentRepository, times(1)).delete(assignmentEntity);
    }

    @Test
    void create_WhenSubjectNotFound_ShouldThrowNotFoundException() {
        CreateAssignmentRequest createAssignmentRequest = CreateAssignmentRequest.builder()
                .isDue("true")
                .points("10")
                .startedDate("2018-12-29 19:34:50")
                .closedDate("2018-12-29 19:34:50")
                .attachment(attachment)
                .build();

        when(teacherService.findById(createAssignmentRequest.getTeacherId())).thenReturn(TeacherEntity.builder().build());
        when(subjectService.findByIdEntity(createAssignmentRequest.getSubjectId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.create(createAssignmentRequest));
    }

    @Test
    void create_WhenTeacherNotFound_ShouldThrowNotFoundException() {
        CreateAssignmentRequest createAssignmentRequest = CreateAssignmentRequest.builder()
                .isDue("true")
                .points("10")
                .startedDate("2018-12-29 19:34:50")
                .closedDate("2018-12-29 19:34:50")
                .attachment(attachment)
                .build();

        when(teacherService.findById(createAssignmentRequest.getTeacherId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> assignmentService.create(createAssignmentRequest));
    }

    @Test
    void findById_WhenAssignmentNotFound_ShouldThrowNotFoundException() {
        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.findById("1"));
    }

    @Test
    void getStudentsTurnedIn_WhenAssignmentNotFound_ShouldReturnNotFoundException() {
        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.getStudentsTurnedIn("1"));
    }

    @Test
    void update_WhenAssignmentNotFound_ShouldThrowNotFoundException() {
        CreateAssignmentRequest createAssignmentRequest = CreateAssignmentRequest.builder()
                .isDue("true")
                .points("10")
                .startedDate("2018-12-29 19:34:50")
                .closedDate("2018-12-29 19:34:50")
                .attachment(attachment)
                .build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.update("1", createAssignmentRequest));
    }

    @Test
    void delete_WhenAssignmentNotFound_ShouldThrowNotFoundException() {
        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.delete("1"));
    }

    @Test
    void submit_WhenAssignmentNotFound_ShouldThrowNotFoundException() {
        Submitting submitting = Submitting.builder().build();
        SubmitRequest submitRequest = SubmitRequest.builder().build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());
        SubmittingResponse submittingResponse = SubmittingResponse.builder().build();


        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.submit("1", submitRequest));
    }

    @Test
    void submit_WhenSubmittingNotFound_ShouldThrowNotFoundException() {
        StudentEntity studentEntity = StudentEntity.builder().build();
        SubmitRequest submitRequest = SubmitRequest.builder().build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.of(AssignmentEntity.builder().build()));
        when(studentRepository.findById("1")).thenReturn(Optional.of(studentEntity));
        when(submittingRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.submit("1", submitRequest));
    }

    @Test
    void submit_WhenStudentNotFound_ShouldThrowNotFoundException() {
        Submitting submitting = Submitting.builder().build();
        SubmittingResponse submittingResponse = SubmittingResponse.builder().build();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder().build();
        SubmitRequest submitRequest = SubmitRequest.builder().build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.of(assignmentEntity));
        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.submit("1", submitRequest));
    }

    @Test
    void getStudentsUnTuredIn_WhenAssignmentNotFound_ShouldThrowNotFoundException() {
        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.getStudentsUnTurnedIn("1"));
    }

    @Test
    void update_WhenSubjectNotFound_ShouldThrowNotFoundException() {
            CreateAssignmentRequest createAssignmentRequest = CreateAssignmentRequest.builder()
                .isDue("true")
                .points("10")
                .startedDate("2018-12-29 19:34:50")
                .closedDate("2018-12-29 19:34:50")
                .attachment(attachment)
                .build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.of(AssignmentEntity.builder().build()));
        when(subjectService.findByIdEntity(createAssignmentRequest.getSubjectId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.update("1", createAssignmentRequest));
    }

    @Test
    void reSubmit_WhenAssignmentNotFound_ShouldThrowNotFoundException() {
        SubmitRequest submitRequest = SubmitRequest.builder().build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.reSubmit("1", submitRequest));
    }

    @Test
    void reSubmit_WhenStudentNotFound_ShouldThrowNotFoundException() {
        SubmitRequest submitRequest = SubmitRequest.builder().studentId("1").build();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder().build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.of(assignmentEntity));
        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.reSubmit("1", submitRequest));
    }

    @Test
    void reSubmit_WhenSubmittingNotFound_ShouldThrowNotFoundException() {
        SubmitRequest submitRequest = SubmitRequest.builder().studentId("1").build();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder().build();
        StudentEntity studentEntity = StudentEntity.builder().build();

        when(assignmentRepository.findById("1")).thenReturn(Optional.of(assignmentEntity));
        when(studentRepository.findById("1")).thenReturn(Optional.of(studentEntity));
        when(submittingRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.reSubmit("1", submitRequest));
    }

    @Test
    void grading_WhenSubbmitingNotFound_ShoudThrowNotFoundException() {
        GradingRequest gradingRequest = new GradingRequest();

        when(submittingRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assignmentService.grading("1", gradingRequest));
    }

    @Test
    void grading_WhenStatusIsNotSubmitted_ShouldThrowRunTimeError() {
        GradingRequest gradingRequest = new GradingRequest();
        Submitting submitting = Submitting.builder().status(SubmitStatus.GRADED).build();

        when(submittingRepository.findById("1")).thenReturn(Optional.of(submitting));

        assertThrows(RuntimeException.class, () -> assignmentService.grading("1", gradingRequest));
    }

    @Test
    void grading_WhenScoreIsGreaterThanPoints_ShouldThrowRunTimeError() {
        GradingRequest gradingRequest = new GradingRequest();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder().points(10).build();
        Submitting submitting = Submitting.builder().status(SubmitStatus.SUBMITTED).score(11).assignment(assignmentEntity).build();

        when(submittingRepository.findById("1")).thenReturn(Optional.of(submitting));

        assertThrows(RuntimeException.class, () -> assignmentService.grading("1", gradingRequest));
    }
}
