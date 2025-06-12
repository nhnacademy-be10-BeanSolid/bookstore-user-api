package com.nhnacademy.bookstoreuserapi.exception;

public class PointTypeNotFoundException extends RuntimeException {
    public PointTypeNotFoundException(Long typeId) {
        super("PointType ID " + typeId + " not found.");
    }
}
