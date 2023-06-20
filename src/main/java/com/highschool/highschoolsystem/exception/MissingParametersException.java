package com.highschool.highschoolsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissingParametersException extends RuntimeException {
    private String message;
}
