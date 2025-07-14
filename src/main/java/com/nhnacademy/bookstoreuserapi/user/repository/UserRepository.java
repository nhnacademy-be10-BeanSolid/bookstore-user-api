package com.nhnacademy.bookstoreuserapi.user.repository;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User findByUserId(String userId);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.lastLoginAt = :lastLoginAt where u.userId = :userId")
    void updateLastLoginByUserId(String userId, LocalDateTime lastLoginAt);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.userPoint = :point + u.userPoint where u.userId = :userId")
    void updatePointByUserId(String userId, int point);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.userStatus = :status where u.userId = :userId")
    void updateStatusByUserId(String userId, User.Status status);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.userGrade = (select ug from UserGrade ug where ug.gradeName = :gradeName) where u.userId = :userId")
    void updateUserGrade_gradeNameByUserId(String userId, UserGrade.Grade gradeName);

    boolean existsByUserId(String userId);

    User findByUserNameAndUserEmail(String userName, String userEmail);

    User findByUserNo(Long userNo);

    @Query("select u.userPoint from User u where u.userId = :userId")
    int findUserPointByUserId(String userId);

}
