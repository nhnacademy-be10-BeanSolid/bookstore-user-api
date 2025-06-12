package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.exception.*;
import com.nhnacademy.bookstoreuserapi.util.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({
            InvalidDataException.class,
            InvalidReviewDataException.class,
            ReviewAlreadyExistsBookException.class,
            AddressAlreadyExistException.class,
            AddressLimitExceededException.class,
            AddressLengthExceededException.class,
            UserAlreadyExistException.class,
            ValidationFailedException.class,
            UserGradeAlreadyExistException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestExceptions(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            ReviewNotFoundException.class,
            AddressNotFoundException.class,
            UserNotFoundException.class,
            UserGradeNotFoundException.class,
            PointTypeNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundExceptions(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
