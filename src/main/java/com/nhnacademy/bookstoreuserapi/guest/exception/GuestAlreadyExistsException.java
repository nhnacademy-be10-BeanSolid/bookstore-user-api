package com.nhnacademy.bookstoreuserapi.guest.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class GuestAlreadyExistsException extends ConflictException {
    public GuestAlreadyExistsException(Long orderId) {
        super("Guest with orderId " + orderId + " already exists.");
    }
}
