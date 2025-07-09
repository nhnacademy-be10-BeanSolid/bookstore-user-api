package com.nhnacademy.bookstoreuserapi.common.exception;

public class ForbiddenException extends CustomHttpException {
    public ForbiddenException(String message) {
        super(CustomHttpStatus.FORBIDDEN, message);
    }
}
