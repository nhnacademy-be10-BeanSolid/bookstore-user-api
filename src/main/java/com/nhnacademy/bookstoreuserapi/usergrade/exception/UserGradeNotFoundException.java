package com.nhnacademy.bookstoreuserapi.usergrade.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class UserGradeNotFoundException extends NotFoundException {
    public UserGradeNotFoundException(String userGradeId) {
        super("UserGrade ID : " + userGradeId + " not found");
    }
}
