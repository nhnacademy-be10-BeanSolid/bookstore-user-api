package com.nhnacademy.bookstoreuserapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AddressAlreadyExistException extends RuntimeException {
    public AddressAlreadyExistException(String addressDetail, String userId) {
        super("Address " + addressDetail + " is already exist in " + userId);
    }
}
