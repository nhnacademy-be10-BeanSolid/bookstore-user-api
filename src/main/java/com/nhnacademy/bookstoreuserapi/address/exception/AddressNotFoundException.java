package com.nhnacademy.bookstoreuserapi.address.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class AddressNotFoundException extends NotFoundException {
    public AddressNotFoundException(long addressId) {
        super("Address ID " + addressId + " not found.");
    }
}
