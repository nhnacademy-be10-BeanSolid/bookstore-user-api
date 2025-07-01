package com.nhnacademy.bookstoreuserapi.address.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class AddressAlreadyExistException extends ConflictException {
    public AddressAlreadyExistException(String addressDetail, String userId) {
        super("Address " + addressDetail + " is already exist in " + userId);
    }
}
