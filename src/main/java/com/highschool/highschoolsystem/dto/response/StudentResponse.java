package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StudentResponse {
    private String id;
    private String name;
    private String fullName;
    private String email;
    private String avatar;
    private String location;
    private String enteredDate;
    private String cardId;
    private String phoneNumber;
    private boolean isAbsent;
}
