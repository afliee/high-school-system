package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
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

    public void deleteAllByStudentId(String id) {
        var submitting = submittingRepository.findAllByStudentId(id);

        if (submitting == null) {
            return;
        }

        submitting.forEach(submitEntity -> {
            var file = submitEntity.getFile();

            if (file != null) {
                try {
                    FileUploadUtils.removeDir(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        submittingRepository.deleteAllByStudentId(id);
    }
}
