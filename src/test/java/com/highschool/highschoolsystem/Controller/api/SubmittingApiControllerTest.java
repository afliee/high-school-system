package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.service.SubmittingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SubmittingApiControllerTest {
    private SubmittingApiController submittingApiController;

    @Mock
    private SubmittingService submittingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        submittingApiController = new SubmittingApiController(submittingService);
    }

    @Test
    public void testGetSubmitting() {
        // Mock the behavior of the submittingService.getSubmitting() method
        String id = "123";
        SubmittingResponse submittingResponse = SubmittingResponse.builder().build();
        when(submittingService.getSubmitting(id)).thenReturn(submittingResponse);

        // Call the getSubmitting() method of the submittingApiController
        ResponseEntity<?> responseEntity = submittingApiController.getSubmitting(id);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(submittingResponse, responseEntity.getBody());
    }
}