package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;

public interface UserService {

    ResponseUser getUser(String userId);

    ResponseUser saveUser(UserCreateRequest request);

    ResponseUser updatePersonalInformation(String userId, UserUpdateRequest request);

    void updateLastLoginAt(String userId);

    void updatePoint(String userId, int point);

    void updateUserStatus(String userId, User.Status status);

    void updateUserGradeName(String userId, String gradeName);

    void deleteUser(String userId);
}
