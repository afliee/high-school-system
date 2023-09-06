package com.highschool.highschoolsystem.Controller.api;

import com.highschool.highschoolsystem.controller.api.AssignmentApiController;
import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.dto.request.GradingRequest;
import com.highschool.highschoolsystem.dto.request.SubmitRequest;
import com.highschool.highschoolsystem.service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class AssignmentApiControllerTest {

    @Mock
    private AssignmentService assignmentService;

    @InjectMocks
    private AssignmentApiController assignmentApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldCallAssignmentServiceCreateMethod() {
        CreateAssignmentRequest request = new CreateAssignmentRequest();
        assignmentApiController.create(request);
        verify(assignmentService).create(request);
    }

    @Test
    void update_shouldCallAssignmentServiceUpdateMethod() {
        String id = "assignmentId";
        CreateAssignmentRequest request = new CreateAssignmentRequest();
        assignmentApiController.update(id, request);
        verify(assignmentService).update(id, request);
    }

    @Test
    void delete_shouldCallAssignmentServiceDeleteMethod() {
        String id = "assignmentId";
        assignmentApiController.delete(id);
        verify(assignmentService).delete(id);
    }

    @Test
    void submit_shouldCallAssignmentServiceSubmitMethod() {
        String id = "assignmentId";
        SubmitRequest request = new SubmitRequest();
        assignmentApiController.submit(id, request);
        verify(assignmentService).submit(id, request);
    }

    @Test
    void reSubmit_shouldCallAssignmentServiceReSubmitMethod() {
        String id = "assignmentId";
        SubmitRequest request = new SubmitRequest();
        assignmentApiController.reSubmit(id, request);
        verify(assignmentService).reSubmit(id, request);
    }

    @Test
    void grade_shouldCallAssignmentServiceGradingMethod() {
        String submittingId = "submittingId";
        GradingRequest request = new GradingRequest();
        assignmentApiController.grade(submittingId, request);
        verify(assignmentService).grading(submittingId, request);
    }
}