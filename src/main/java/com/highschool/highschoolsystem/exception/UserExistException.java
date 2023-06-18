package com.highschool.highschoolsystem.exception;

import com.highschool.highschoolsystem.util.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExistException extends RuntimeException {
    private String message;
}
