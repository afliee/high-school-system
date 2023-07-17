package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SubjectResponse {
    private String id;
    private String name;
    private String description;
    private String department;
    private TeacherResponse teacher;
    private String color;
    private Date createdAt;
    private DepartmentResponse departmentDetail;
}
