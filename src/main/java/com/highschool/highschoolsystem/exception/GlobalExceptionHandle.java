package com.highschool.highschoolsystem.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(ExpiredJwtException.class)
    public String handleExpiredJwtException(ExpiredJwtException e) {
        return "redirect:/auth/admin/login";
    }
}
