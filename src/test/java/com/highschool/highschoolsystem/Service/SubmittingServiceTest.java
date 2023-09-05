package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.entity.Submitting;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SubmittingServiceTest {
    @Mock
    private SubmittingRepository submittingRepository;

    @InjectMocks
    private SubmittingService submittingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSubmitting() {
        String id = "exampleId";
        SubmittingResponse expectedResponse = new SubmittingResponse();
        when(submittingRepository.findById(id)).thenReturn(Optional.of(new Submitting()));

        SubmittingResponse actualResponse = submittingService.getSubmitting(id);

        assertEquals(expectedResponse, actualResponse);
        verify(submittingRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteAllByStudentId() throws Exception {
        String id = "exampleId";
        Submitting submittingEntity = new Submitting();
        submittingEntity.setFile("exampleFile");
        List<Submitting> submittingEntities = Collections.singletonList(submittingEntity);
        when(submittingRepository.findAllByStudentId(id)).thenReturn(submittingEntities);

        submittingService.deleteAllByStudentId(id);

        verify(submittingRepository, times(1)).findAllByStudentId(id);
        verify(submittingRepository, times(1)).deleteAllByStudentId(id);
    }
}