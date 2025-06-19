package com.nhnacademy.bookstoreuserapi.util;

import org.springframework.security.access.AccessDeniedException;

public class OwnerShipValidator {
    public static void validate(String actualUserId, String expectedUserId) {
        if (!actualUserId.equals(expectedUserId)) {
            throw new AccessDeniedException("요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다.");
        }
    }
}