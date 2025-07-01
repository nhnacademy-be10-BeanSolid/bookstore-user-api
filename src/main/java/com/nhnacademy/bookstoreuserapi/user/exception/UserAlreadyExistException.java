package com.nhnacademy.bookstoreuserapi.user.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class UserAlreadyExistException extends ConflictException {
    public UserAlreadyExistException(String userId) {
        super("User ID : " + userId + " already exists");
    }
}
