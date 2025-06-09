package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.exception.AddressAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.AddressLengthExceededException;
import com.nhnacademy.bookstoreuserapi.exception.AddressLimitExceededException;
import com.nhnacademy.bookstoreuserapi.exception.AddressNotFoundException;
import com.nhnacademy.bookstoreuserapi.util.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler({AddressAlreadyExistException.class, AddressLimitExceededException.class, AddressLengthExceededException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleAddressExceptions(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AddressNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleAddressNotFoundExceptions(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
