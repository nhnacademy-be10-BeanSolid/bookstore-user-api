package com.nhnacademy.bookstoreuserapi.guest.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class GuestNotFoundException extends NotFoundException {
    public GuestNotFoundException(Long orderId) {
        super("Guest not found with orderId: " + orderId);
    }
}