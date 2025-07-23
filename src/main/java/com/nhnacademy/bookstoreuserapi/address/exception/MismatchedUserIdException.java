package com.nhnacademy.bookstoreuserapi.address.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ForbiddenException;

public class MismatchedUserIdException extends ForbiddenException {
    public MismatchedUserIdException(String message) {
        super(message);
    }
}
