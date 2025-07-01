package com.nhnacademy.bookstoreuserapi.address.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(long addressId) {
        super("Address ID " + addressId + " not found.");
    }
}
