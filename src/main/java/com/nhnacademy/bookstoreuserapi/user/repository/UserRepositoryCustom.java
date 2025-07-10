package com.nhnacademy.bookstoreuserapi.user.repository;

import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;

import java.util.List;

public interface UserRepositoryCustom {
    long bulkUpdateUserGrade(UserGrade grade, List<Long> userNos);


    List<Long> findUserNosWithDifferentGrade(List<Long> userNos, UserGrade.Grade gradeName);
}
