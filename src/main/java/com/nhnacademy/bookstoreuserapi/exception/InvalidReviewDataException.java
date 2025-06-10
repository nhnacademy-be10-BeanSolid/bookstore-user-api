package com.nhnacademy.bookstoreuserapi.exception;

public class InvalidReviewDataException extends RuntimeException {
    public InvalidReviewDataException(String message) {
        super(message);
    }
}
