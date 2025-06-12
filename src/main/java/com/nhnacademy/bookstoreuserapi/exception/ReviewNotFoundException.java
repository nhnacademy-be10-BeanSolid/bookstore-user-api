package com.nhnacademy.bookstoreuserapi.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(long reviewId) {
        super("Review ID: " + reviewId + " not found");
    }
}
