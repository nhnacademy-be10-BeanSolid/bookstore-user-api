package com.nhnacademy.bookstoreuserapi.exception;

public class ConflictException extends CustomHttpException {
    public ConflictException(String message) {
        super(CustomHttpStatus.CONFLICT, message);
    }
}
