package com.nhnacademy.bookstoreuserapi.Service;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(String userId);

    void saveUser(User user);

    void updatePersonalInformation(User user);

    void updateLastLoginAt(String userId);

    void updatePoint(String userId, int point);

    void updateUserStatus(String userId, User.Status status);

    // 나중에 구현
    // void updateUserGradeId(String userId, long gradeId);

    void deleteUser(String userId);
}
