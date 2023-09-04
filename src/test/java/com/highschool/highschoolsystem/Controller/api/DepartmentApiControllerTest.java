package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.DepartmentRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnDepartmentResponse() {
        DepartmentRequest request = new DepartmentRequest();
        DepartmentResponse department = new DepartmentResponse();
        when(departmentService.save(request)).thenReturn(department);


        ResponseEntity<?> response = departmentController.create(request);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == department;
        verify(departmentService).save(request);
    }

    @Test
    void update_withValidId_shouldReturnDepartmentResponse() {
        String id = "departmentId";
        DepartmentRequest request = new DepartmentRequest();
        DepartmentResponse department = new DepartmentResponse();
        when(departmentService.update(id, request)).thenReturn(department);

        ResponseEntity<?> response = departmentController.update(id, request);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == department;
        verify(departmentService).update(id, request);
    }

    @Test
    void update_withInvalidId_shouldReturnNotFoundStatus() {
        String id = "invalidId";
        DepartmentRequest request = new DepartmentRequest();
        when(departmentService.update(id, request)).thenReturn(null);

        ResponseEntity<?> response = departmentController.update(id, request);

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        verify(departmentService).update(id, request);
    }

    @Test
    void show_withValidId_shouldReturnDepartmentResponse() {
        String id = "departmentId";
        DepartmentResponse department = new DepartmentResponse();
        when(departmentService.findById(id)).thenReturn(department);

        ResponseEntity<?> response = departmentController.show(id);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == department;
        verify(departmentService).findById(id);
    }

    @Test
    void show_withInvalidId_shouldReturnNotFoundStatus() {
        String id = "invalidId";
        when(departmentService.findById(id)).thenReturn(null);

        ResponseEntity<?> response = departmentController.show(id);

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        verify(departmentService).findById(id);
    }
}