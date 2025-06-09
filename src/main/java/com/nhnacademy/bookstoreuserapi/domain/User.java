package com.nhnacademy.bookstoreuserapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    public enum Status {
        ACTIVE,
        DORMANT,
        WITHDRAWN
    }

    @Id
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_phoneNumber", nullable = false)
    private String userPhoneNumber;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_birth", nullable = false)
    private LocalDate userBirth;

    @Column(name = "user_point", nullable = false)
    private int userPoint;

    @Column(name = "is_auth", nullable = false)
    private boolean isAuth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private Status userStatus;

    @Column(name = "last_login_at", nullable = false)
    private LocalDateTime lastLoginAt;

    // 다대일 매핑 필요
//    @Column(name = "usergrade_id", nullable = false)
//    private Long userGradeId;

    public User(String userId, String userPassword, String userName, String userPhoneNumber, String userEmail, LocalDate userBirth) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userBirth = userBirth;
    }
}
