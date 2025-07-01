package com.nhnacademy.bookstoreuserapi.review.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException(long reviewId) {
        super("Review ID: " + reviewId + " not found");
    }
}
