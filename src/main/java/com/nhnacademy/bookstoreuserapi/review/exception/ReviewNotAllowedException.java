package com.nhnacademy.bookstoreuserapi.review.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ForbiddenException;

public class ReviewNotAllowedException extends ForbiddenException {
    public ReviewNotAllowedException(String message) {
        super(message);
    }
}
