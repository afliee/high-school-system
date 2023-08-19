package com.highschool.highschoolsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest {
    private String cardId;
    private String email;
    private String location;
    private String phone;
    private String birthday;
    private String gender;
    private MultipartFile avatar;
}
