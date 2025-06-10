package com.nhnacademy.bookstoreuserapi.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(long addressId) {
        super("Address with ID " + addressId + " not found.");
    }
}
