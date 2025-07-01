package com.nhnacademy.bookstoreuserapi.review.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class ReviewAlreadyExistsBookException extends ConflictException {
    public ReviewAlreadyExistsBookException(String userId, long bookId) {
        super("Review already exists for user: " + userId + " at book: " + bookId);
    }
}
