package com.nhnacademy.bookstoreuserapi.user.service;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.domain.Oauth2UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUserId;

public interface UserService {

    ResponseUser getUser(String userId);

    ResponseUser getUserByUserNo(Long userNo);

    ResponseUserId getUserIdByUserNameAndUserEmail(String userName, String userEmail);

    ResponseUser saveUser(UserCreateRequest request);

    ResponseUser saveOauth2User(Oauth2UserCreateRequest request);

    ResponseUser updatePersonalInformation(String userId, UserUpdateRequest request);

    ResponseUser updateLastLoginAt(String userId);

    ResponseUser updatePoint(String userId, int point);

    ResponseUser updateUserStatus(String userId, User.Status status);

    ResponseUser updateUserGradeName(String userId, String gradeName);

    ResponseUser deleteUser(String userId);

    boolean isUserExist(String userId);
}
