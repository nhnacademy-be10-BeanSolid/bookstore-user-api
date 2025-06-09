package com.nhnacademy.bookstoreuserapi.exception;

public class AddressLengthExceededException extends RuntimeException {
    public AddressLengthExceededException(String message) {
        super(message);
    }
}
