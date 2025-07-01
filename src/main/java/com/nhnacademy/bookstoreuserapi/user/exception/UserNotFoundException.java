package com.nhnacademy.bookstoreuserapi.user.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
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
