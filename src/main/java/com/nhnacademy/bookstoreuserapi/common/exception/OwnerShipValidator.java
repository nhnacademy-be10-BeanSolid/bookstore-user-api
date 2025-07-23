package com.nhnacademy.bookstoreuserapi.common.exception;

import com.nhnacademy.bookstoreuserapi.address.exception.MismatchedUserIdException;

public class OwnerShipValidator {
    private OwnerShipValidator() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static void validate(String actualUserId, String expectedUserId) {
        if (!actualUserId.equals(expectedUserId)) {
            throw new MismatchedUserIdException("요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다.");
        }
    }
}