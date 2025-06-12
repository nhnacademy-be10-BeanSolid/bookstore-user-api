package com.nhnacademy.bookstoreuserapi.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(long cartId) {
        super("Cart not found with ID: " + cartId);
    }
}
