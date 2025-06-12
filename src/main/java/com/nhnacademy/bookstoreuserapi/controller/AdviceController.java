package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.exception.*;
import com.nhnacademy.bookstoreuserapi.util.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler({InvalidDataException.class, InvalidReviewDataException.class, ReviewAlreadyExistsBookException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidDataException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ReviewNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleReviewNotFoundException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({AddressAlreadyExistException.class, AddressLimitExceededException.class, AddressLengthExceededException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleAddressException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AddressNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleAddressNotFoundException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleUserNotFoundException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleUserAlreadyExistException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationFailedException(ValidationFailedException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserGradeAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleUserGradeAlreadyExistException(UserGradeAlreadyExistException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserGradeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleUserGradeNotFoundException(UserGradeNotFoundException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleCartNotFoundException(CartNotFoundException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleCartAlreadyExistException(CartAlreadyExistException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
