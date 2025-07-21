package com.nhnacademy.bookstoreuserapi.user.repository;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

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

    boolean existsByUserId(String userId);

    User findByUserNameAndUserEmail(String userName, String userEmail);

    @Query("select u.userPoint from User u where u.userId = :userId")
    int findUserPointByUserId(String userId);

    @Query("select u.userId from User u where u.userNo = :userNo")
    String findUserIdByUserNo(Long userNo);

    Page<User> findByUserStatusAndLastLoginAtBefore(User.Status status, LocalDateTime threeMonthsAgo, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.userStatus = 'DORMANT' WHERE u.userNo IN :userIds")
    void updateStatusForUsers(List<Long> userIds);

    @Query("SELECT u FROM User u WHERE FUNCTION('MONTH', u.userBirth) = :month AND FUNCTION('DAY', u.userBirth) = :day")
    List<User> findByBirthMonthAndBirthDay(int month, int day);
}
