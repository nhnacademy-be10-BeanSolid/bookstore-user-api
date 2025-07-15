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

    ResponseUser plusPoint(String userId, int point);

    ResponseUser minusPoint(String userId, int point);

    ResponseUser updateUserStatus(String userId, User.Status status);

    void bulkUpdateUserGrades();

    ResponseUser deleteUser(String userId);

    boolean isUserExist(String userId);

    int getUserPoint(String userId);

    
}
