package com.nhnacademy.bookstoreuserapi.address.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.BadRequestException;

public class AddressLimitExceededException extends BadRequestException {
    public AddressLimitExceededException(String userId) {
        super("User " + userId + " already has the maximum of 10 addresses.");
    }
}
