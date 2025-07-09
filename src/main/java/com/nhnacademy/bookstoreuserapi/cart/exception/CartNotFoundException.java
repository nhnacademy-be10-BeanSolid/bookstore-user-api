package com.nhnacademy.bookstoreuserapi.cart.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class CartNotFoundException extends NotFoundException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
