package com.highschool.highschoolsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaultRequest {
    private String[]  faults;

//    @NotEmpty(message = "Teacher id must not be empty")
//    @NotNull(message = "Teacher id must not be null")
//    private String teacherId;

    @NotNull(message = "Student id must not be null")
    @NotEmpty(message = "Student id must not be empty")
    private String studentId;

    @NotNull(message = "Subject id must not be null")
    @NotEmpty(message = "Subject id must not be empty")
    private String subjectId;

    private String otherFault;
}
