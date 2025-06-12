package com.nhnacademy.bookstoreuserapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddressLengthExceededException extends RuntimeException {
    public AddressLengthExceededException() {
        super("주소는 255자 이내여야 합니다.");
    }
}
