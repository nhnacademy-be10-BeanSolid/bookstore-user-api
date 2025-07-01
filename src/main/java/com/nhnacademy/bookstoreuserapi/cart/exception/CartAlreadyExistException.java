package com.nhnacademy.bookstoreuserapi.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CartAlreadyExistException extends RuntimeException {
    public CartAlreadyExistException(String userId, long bookId) {
        super("Cart already exists for user: " + userId + " at book: " + bookId);
    }
}
