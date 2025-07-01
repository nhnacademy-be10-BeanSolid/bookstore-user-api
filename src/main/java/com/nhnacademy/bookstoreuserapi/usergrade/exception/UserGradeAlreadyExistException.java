package com.nhnacademy.bookstoreuserapi.usergrade.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.ConflictException;

public class UserGradeAlreadyExistException extends ConflictException {
    public UserGradeAlreadyExistException(String userGrade) {
        super("Grade : " + userGrade + " already exists.");
    }
}
