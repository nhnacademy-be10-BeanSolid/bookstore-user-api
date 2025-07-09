package com.nhnacademy.bookstoreuserapi.pointtype.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ForbiddenException;

public class PointTypeNotAvailableException extends ForbiddenException {
    public PointTypeNotAvailableException(String typeName) {
        super("typeName: " + typeName + " is not available");
    }
}
