package com.highschool.highschoolsystem.dto.response;

import com.highschool.highschoolsystem.entity.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassResponse {
    private String id;
    private String name;
    private Integer present;
    private String createdDate;
    private List<StudentResponse> students;
    private String chairman;

}
