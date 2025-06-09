package com.nhnacademy.bookstoreuserapi.exception;

public class AddressAlreadyExistException extends RuntimeException {
    public AddressAlreadyExistException(String addressDetail, String userId) {
        super("Address " + addressDetail + " is already exist in " + userId);
    }
}
