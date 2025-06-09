package com.nhnacademy.bookstoreuserapi.exception;

public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(String userId) {
        super("User " + userId + " already has the maximum of 10 addresses.");
    }
}
