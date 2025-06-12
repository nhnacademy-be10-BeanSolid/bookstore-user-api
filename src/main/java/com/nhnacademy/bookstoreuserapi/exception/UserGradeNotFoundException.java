package com.nhnacademy.bookstoreuserapi.exception;

public class UserGradeNotFoundException extends RuntimeException {
    public UserGradeNotFoundException(String userGradeId) {
        super("UserGrade ID : " + userGradeId + " not found");
    }
}
