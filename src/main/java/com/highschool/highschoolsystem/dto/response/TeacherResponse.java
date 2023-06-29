package com.highschool.highschoolsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherResponse {
    private String name;
    private String fullName;
    private String avatar;
    private String email;
    private String cardId;
    private boolean gender;
    private String phone;
    private String address;
    private String birthday;
    private Long salary;
}
