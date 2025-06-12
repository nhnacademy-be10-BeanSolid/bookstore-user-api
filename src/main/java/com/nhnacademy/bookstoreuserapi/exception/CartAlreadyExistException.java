package com.nhnacademy.bookstoreuserapi.exception;

public class CartAlreadyExistException extends RuntimeException {
    public CartAlreadyExistException(String userId, long bookId) {
        super("Cart already exists for user: " + userId + " at book: " + bookId);
    }
}
