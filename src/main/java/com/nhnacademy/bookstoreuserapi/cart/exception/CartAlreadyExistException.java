package com.nhnacademy.bookstoreuserapi.cart.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class CartAlreadyExistException extends ConflictException {
    public CartAlreadyExistException(String userId, long bookId) {
        super("Cart already exists for user: " + userId + " at book: " + bookId);
    }
}
