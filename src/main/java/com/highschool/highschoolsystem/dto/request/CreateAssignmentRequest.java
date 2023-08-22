package com.highschool.highschoolsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAssignmentRequest {
    @NotNull(message = "Title must not be null")
    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Description must not be empty")
    @NotNull(message = "Description must not be null")
    private String description;
    private String isDue;
    private String points;
    private String startedDate;
    private String closedDate;
    private MultipartFile attachment;

    @NotEmpty(message = "Teacher id must not be empty")
    @NotNull(message = "Teacher id must not be null")
    private String teacherId;

    @NotEmpty(message = "Subject id must not be empty")
    @NotNull(message = "Subject id must not be null")
    private String subjectId;
}
