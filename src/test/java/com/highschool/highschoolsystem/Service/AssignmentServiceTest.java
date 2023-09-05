package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.SubmitStatus;
import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.dto.request.GradingRequest;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.entity.AssignmentEntity;
import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.entity.Submitting;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.repository.AssignmentRepository;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

//    @Test
//    void create_WhenSubjectNotFound_ShoulThrowNotFoundException() {
//        when()
//    }
}
