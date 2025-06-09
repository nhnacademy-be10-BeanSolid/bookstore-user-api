package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, String> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.lastLoginAt = :lastLoginAt where u.userId = :userId")
    void updateLastLoginByUserId(String userId, LocalDateTime lastLoginAt);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.userPoint = :point + u.userPoint where u.userId = :userId")
    void updatePointByUserId(String userId, int point);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.userStatus = :status where u.userId = :userId")
    void updateStatusByUserId(String userId, User.Status status);
}
