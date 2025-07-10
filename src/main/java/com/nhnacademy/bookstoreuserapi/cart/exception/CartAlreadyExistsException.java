package com.nhnacademy.bookstoreuserapi.cart.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class CartAlreadyExistsException extends ConflictException {
    public CartAlreadyExistsException(String userId) {
        super("Cart already exists for user: " + userId);
    }
}
