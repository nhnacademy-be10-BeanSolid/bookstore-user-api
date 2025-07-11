package com.nhnacademy.bookstoreuserapi.user.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.BadRequestException;

public class PointNotEnoughException extends BadRequestException {
    public PointNotEnoughException(String userId, int point) {
        super("UserId: " + userId + " has insufficient points. (Requested: " + point + ")");
    }
}
