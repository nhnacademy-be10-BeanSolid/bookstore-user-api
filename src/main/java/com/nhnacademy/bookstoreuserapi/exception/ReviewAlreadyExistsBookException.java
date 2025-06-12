package com.nhnacademy.bookstoreuserapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReviewAlreadyExistsBookException extends RuntimeException {
    public ReviewAlreadyExistsBookException(String userId, long bookId) {
        super("Review already exists for user: " + userId + " at book: " + bookId);
    }
}
