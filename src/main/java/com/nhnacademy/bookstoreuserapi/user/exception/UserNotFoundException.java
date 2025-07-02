package com.nhnacademy.bookstoreuserapi.user.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final String NOT_FOUND = " not found";

    public UserNotFoundException(String userId) {
        super("User ID : " + userId + NOT_FOUND);
    }
    public UserNotFoundException(Long userNo) {
        super("User No : " + userNo + NOT_FOUND);
    }
    public UserNotFoundException(String userName, String userEmail) {
        super("User Name : " + userName + " Email : " + userEmail + NOT_FOUND);
    }
}


