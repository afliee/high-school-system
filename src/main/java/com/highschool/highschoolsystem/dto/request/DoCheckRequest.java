package com.highschool.highschoolsystem.dto.request;

import com.highschool.highschoolsystem.config.RegisterStatus;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoCheckRequest {
    private String id;
    private int status;
}
