package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.LevelRequest;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.service.LevelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LevelApiControllerTest {

    @Mock
    private LevelService levelService;

    @InjectMocks
    private LevelApiController levelApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnListOfLevelResponses() {
        // Arrange
        boolean isDetail = false;
        List<LevelResponse> expectedResponses = Arrays.asList(
                LevelResponse.builder().build(), LevelResponse.builder().build()
        );
        when(levelService.findAll(isDetail)).thenReturn(expectedResponses);

        // Act
        ResponseEntity<List<LevelResponse>> responseEntity = levelApiController.getAll(isDetail);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponses, responseEntity.getBody());
        verify(levelService, times(1)).findAll(isDetail);
    }

    @Test
    void create_shouldReturnCreatedLevelResponse() {
        // Arrange
        LevelRequest levelRequest = LevelRequest.builder().build();
        LevelResponse expectedResponse = LevelResponse.builder().build();
        when(levelService.create(levelRequest)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<LevelResponse> responseEntity = levelApiController.create(levelRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(levelService, times(1)).create(levelRequest);
    }
}