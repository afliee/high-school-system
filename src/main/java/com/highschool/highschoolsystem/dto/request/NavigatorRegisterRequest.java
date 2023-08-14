package com.highschool.highschoolsystem.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"createdAt"})
public class NavigatorRegisterRequest {
    private String id;
    private LocalDate createdAt;
}
