package com.nhnacademy.bookstoreuserapi.exception;

public class UserGradeNotFoundException extends RuntimeException {
    public UserGradeNotFoundException(String userGradeId) {
        super("해당 등급이 존재하지 않습니다: " + userGradeId);
    }
}
