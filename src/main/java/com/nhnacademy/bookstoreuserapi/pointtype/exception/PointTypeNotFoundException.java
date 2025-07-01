package com.nhnacademy.bookstoreuserapi.pointtype.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PointTypeNotFoundException extends RuntimeException {
    public PointTypeNotFoundException(Long typeId) {
        super("PointType ID " + typeId + " not found.");
    }
}
