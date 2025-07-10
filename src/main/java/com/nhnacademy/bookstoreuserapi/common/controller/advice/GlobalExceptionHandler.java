package com.nhnacademy.bookstoreuserapi.common.controller.advice;

import com.nhnacademy.bookstoreuserapi.common.exception.CustomHttpException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<ErrorMessage> handleException(CustomHttpException e, HttpServletRequest request) {

        int statusCode = e.getCustomHttpStatus().getCode();
        String reasonPhrase = e.getCustomHttpStatus().name();

        ErrorMessage errorMessage = new ErrorMessage(
                statusCode,
                reasonPhrase,
                request.getRequestURI(),
                e.getMessage()
        );
        return ResponseEntity.status(statusCode).body(errorMessage);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorMessage> handleException(MissingRequestHeaderException e, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e, HttpServletRequest request) {
        CustomHttpException.CustomHttpStatus status = CustomHttpException.CustomHttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = new ErrorMessage(
                status.getCode(),
                status.name(),
                request.getRequestURI(),
                e.getMessage()
        );
        return ResponseEntity.status(status.getCode()).body(errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        int statusCode = HttpStatus.CONFLICT.value();
        String reasonPhrase = HttpStatus.CONFLICT.getReasonPhrase();
        ErrorMessage errorMessage = new ErrorMessage(
                statusCode,
                reasonPhrase,
                request.getRequestURI(),
                Objects.requireNonNull(e.getRootCause()).getMessage()
        );
        return ResponseEntity.status(statusCode).body(errorMessage);
    }
}
