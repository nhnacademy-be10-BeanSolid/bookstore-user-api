package com.nhnacademy.bookstoreuserapi.usergrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserGradeNotFoundException extends RuntimeException {
    public UserGradeNotFoundException(String userGradeId) {
        super("UserGrade ID : " + userGradeId + " not found");
    }
}
