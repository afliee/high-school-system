package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SubmittingService {
    @Autowired
    private SubmittingRepository submittingRepository;

    public SubmittingResponse getSubmitting(String id) {
        return SubmittingConverter.toResponse(submittingRepository.findById(id).orElse(null));
    }
}
