package com.nhnacademy.bookstoreuserapi.usergrade.service;

import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.ResponseUserGrade;
import java.util.List;

public interface UserGradeService {

    ResponseUserGrade saveUserGrade(UserGradeCreateRequest userGrade);

    ResponseUserGrade updateUserGrade(String gradeName, UserGradeUpdateRequest userGrade);

    ResponseUserGrade getUserGrade(String gradeName);

    void deleteUserGrade(String gradeName);

    List<ResponseUserGrade> getAllUserGrades();
}
