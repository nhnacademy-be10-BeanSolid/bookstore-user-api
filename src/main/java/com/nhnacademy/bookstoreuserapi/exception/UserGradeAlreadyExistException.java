package com.nhnacademy.bookstoreuserapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserGradeAlreadyExistException extends RuntimeException {
    public UserGradeAlreadyExistException(String userGrade) {
        super("Grade : " + userGrade + " already exists.");
    }
}
