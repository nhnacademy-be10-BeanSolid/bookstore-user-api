package com.nhnacademy.bookstoreuserapi.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(long addressId) {
        super("Address ID " + addressId + " not found.");
    }
}
