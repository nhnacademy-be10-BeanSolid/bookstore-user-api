package com.nhnacademy.bookstoreuserapi.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String userId) {
        super("User ID : " + userId + " already exists");
    }
}
