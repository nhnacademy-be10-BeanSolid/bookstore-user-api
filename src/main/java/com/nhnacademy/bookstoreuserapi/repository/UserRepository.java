package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {

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

}
