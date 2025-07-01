package com.nhnacademy.bookstoreuserapi.pointtype.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class PointTypeNotFoundException extends NotFoundException {
    public PointTypeNotFoundException(Long typeId) {
        super("PointType ID " + typeId + " not found.");
    }
}
