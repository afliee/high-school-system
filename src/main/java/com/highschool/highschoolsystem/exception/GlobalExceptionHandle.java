package com.highschool.highschoolsystem.exception;

import com.highschool.highschoolsystem.util.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorMessage> handleUserExistException(Exception e) {
        return ResponseEntity.status(400).body(new ErrorMessage(400, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(Exception e) {
        return ResponseEntity.status(404).body(new ErrorMessage(404, e.getMessage()));
    }
    @ExceptionHandler(MissingParametersException.class)
    public ResponseEntity<ErrorMessage> handleMissingParametersException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorMessage(500, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorMessage(500, e.getMessage()));
    }
}