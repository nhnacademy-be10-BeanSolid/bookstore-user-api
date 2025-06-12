package com.nhnacademy.bookstoreuserapi.exception;

public class AddressLengthExceededException extends RuntimeException {
    public AddressLengthExceededException() {
        super("주소는 255자 이내여야 합니다.");
    }
}
