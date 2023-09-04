package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.SubjectRequest;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SubjectApiControllerTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectApiController subjectApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get_shouldReturnResponseEntityWithSubject() {
        // Arrange
        String subjectId = "123";
        SubjectResponse subjectResponse = SubjectResponse.builder().build();
        when(subjectService.get(subjectId)).thenReturn(subjectResponse);

        // Act
        ResponseEntity<?> responseEntity = subjectApiController.get(subjectId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subjectResponse, responseEntity.getBody());
        verify(subjectService, times(1)).get(subjectId);
    }


    @Test
    void create_shouldReturnResponseEntityWithCreatedSubject() {
        // Arrange
        SubjectRequest subjectRequest = new SubjectRequest();
        SubjectResponse createdSubject = SubjectResponse.builder().build();
        when(subjectService.create(subjectRequest)).thenReturn(createdSubject);

        // Act
        ResponseEntity<?> responseEntity = subjectApiController.create(subjectRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createdSubject, responseEntity.getBody());
        verify(subjectService, times(1)).create(subjectRequest);
    }

    @Test
    void update_shouldReturnResponseEntityWithUpdatedSubject() {
        // Arrange
        String subjectId = "123";
        SubjectRequest subjectRequest = new SubjectRequest();
        SubjectResponse updatedSubject = SubjectResponse.builder().build();
        when(subjectService.update(subjectId, subjectRequest)).thenReturn(updatedSubject);

        // Act
        ResponseEntity<?> responseEntity = subjectApiController.update(subjectId, subjectRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedSubject, responseEntity.getBody());
        verify(subjectService, times(1)).update(subjectId, subjectRequest);
    }

    @Test
    void delete_shouldReturnResponseEntityWithDeletedSubject() {
        // Arrange
        String subjectId = "123";
        SubjectResponse deletedSubject = SubjectResponse.builder().build();
        when(subjectService.delete(subjectId)).thenReturn(deletedSubject);

        // Act
        ResponseEntity<?> responseEntity = subjectApiController.delete(subjectId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(deletedSubject, responseEntity.getBody());
        verify(subjectService, times(1)).delete(subjectId);
    }
}