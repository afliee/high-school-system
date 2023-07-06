package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherResponse {
    private String id;
    private String name;
    private String fullName;
    private String avatar;
    private String email;
    private String cardId;
    private boolean gender;
    private String phone;
    private String address;
    private String birthday;
    private String createdDate;
    private String departmentId;
    private String departmentName;
    private Long salary;
}
