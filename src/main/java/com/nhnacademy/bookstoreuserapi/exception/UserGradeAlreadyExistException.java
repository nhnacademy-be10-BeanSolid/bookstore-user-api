package com.nhnacademy.bookstoreuserapi.exception;

public class UserGradeAlreadyExistException extends RuntimeException {
    public UserGradeAlreadyExistException(String userGrade) {
        super("Grade : " + userGrade + " already exists.");
    }
}
