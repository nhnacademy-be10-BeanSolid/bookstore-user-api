package com.nhnacademy.bookstoreuserapi.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User ID : " + userId + " not found");
    }
}
