package com.nhnacademy.bookstoreuserapi.exception;

public class ReviewAlreadyExistsBookException extends RuntimeException {
    public ReviewAlreadyExistsBookException(String userId, long bookId) {
        super("Review already exists for user: " + userId + " at book: " + bookId);
    }
}
