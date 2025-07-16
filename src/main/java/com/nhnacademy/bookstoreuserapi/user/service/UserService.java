package com.nhnacademy.bookstoreuserapi.user.service;

import com.nhnacademy.bookstoreuserapi.user.domain.*;

public interface UserService {

    ResponseUser getUser(String userId);

    ResponseUser getUserByUserNo(Long userNo);

    ResponseUserId getUserIdByUserNameAndUserEmail(String userName, String userEmail);

    ResponseUser saveUser(UserCreateRequest request);

    ResponseUser saveOauth2User(Oauth2UserCreateRequest request);

    ResponseUser updatePersonalInformation(String userId, UserUpdateRequest request);

    ResponseUser updateLastLoginAt(String userId);

    ResponseUser plusPoint(Long userNo, int point);

    ResponseUser minusPoint(Long userNo, int point);

    ResponseUser updateUserStatus(String userId, User.Status status);

    void bulkUpdateUserGrades();

    ResponseUser deleteUser(String userId);

    boolean isUserExist(String userId);

    int getUserPoint(String userId);

    int getUserPointByUserNo(Long userNo);

    com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade getUserGradeByUserNo(Long userNo);
}