package com.nhnacademy.bookstoreuserapi.cart.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class GuestCartCreationException extends ConflictException {
    public GuestCartCreationException(String message) {
        super(message);
    }
}
