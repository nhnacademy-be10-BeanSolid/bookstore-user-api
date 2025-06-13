package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import java.util.List;

public interface UserGradeService {

    ResponseUserGrade saveUserGrade(UserGradeCreateRequest userGrade);

    ResponseUserGrade updateUserGrade(String gradeName, UserGradeUpdateRequest userGrade);

    ResponseUserGrade getUserGrade(String gradeName);

    void deleteUserGrade(String gradeName);

    List<ResponseUserGrade> getAllUserGrades();
}
