package com.nhnacademy.bookstoreuserapi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidHeaderException extends RuntimeException {
    public static final String USER_ID_BLANK = "User ID must not be null or blank";
    public static final String USER_ID_TOO_LONG = "User ID must not exceed 20 characters";

    public InvalidHeaderException(String message) {
        super(message);
    }
}
