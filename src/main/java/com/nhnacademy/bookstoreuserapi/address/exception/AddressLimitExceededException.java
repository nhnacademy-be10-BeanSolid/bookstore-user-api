package com.nhnacademy.bookstoreuserapi.address.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(String userId) {
        super("User " + userId + " already has the maximum of 10 addresses.");
    }
}
