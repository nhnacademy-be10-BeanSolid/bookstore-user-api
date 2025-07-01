package com.nhnacademy.bookstoreuserapi.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(long reviewId) {
        super("Review ID: " + reviewId + " not found");
    }
}
