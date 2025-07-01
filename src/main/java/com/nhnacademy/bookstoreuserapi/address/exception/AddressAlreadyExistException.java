package com.nhnacademy.bookstoreuserapi.address.exception;

import com.nhnacademy.bookstoreuserapi.exception.CustomHttpException;

public class AddressAlreadyExistException extends CustomHttpException {
    public AddressAlreadyExistException(String addressDetail, String userId) {
        super(CustomHttpStatus.CONFLICT, "Address " + addressDetail + " is already exist in " + userId);
    }
}
