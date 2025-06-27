package com.nhnacademy.bookstoreuserapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User ID : " + userId + " not found");
    }
    public UserNotFoundException(Long userNo) {
        super("User No : " + userNo + " not found");
    }
    public UserNotFoundException(String userName, String userEmail) {
        super("User Name : " + userName + " Email : " + userEmail + " not found");
    }

}
