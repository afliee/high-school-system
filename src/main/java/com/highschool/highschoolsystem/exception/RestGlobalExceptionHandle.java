package com.highschool.highschoolsystem.exception;

import com.highschool.highschoolsystem.util.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Objects;

@RestControllerAdvice
public class RestGlobalExceptionHandle {
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorMessage> handleUserExistException(Exception e) {
        return ResponseEntity.status(400).body(new ErrorMessage(400, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNotFoundException(Exception e) {
        return ResponseEntity.status(404).body(new ErrorMessage(404, e.getMessage()));
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

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
//        handle exception with preset Content-Type = 'text/event-stream' of Stompjs
        return ResponseEntity.status(500).body(e.getMessage());
//        return ResponseEntity.status(500).body(new ErrorMessage(500, e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleBindException(BindException e) {
        String message;

        if (e.getBindingResult().hasErrors()) {
            message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        } else {
            message = e.getMessage();
        }
        return ResponseEntity.status(400).body(new ErrorMessage(400, message));
    }

    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleTokenInvalidException(TokenInvalidException e) {
        return ResponseEntity.status(400).body(new ErrorMessage(400, e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<ErrorMessage> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(417).body(new ErrorMessage(417, "File too large!"));
    }
}
