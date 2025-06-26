package com.nhnacademy.bookstoreuserapi.domain.response;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUser {
    private Long userNo;
    private String userId;
    private String userPassword;
    private String userName;
    private String userPhoneNumber;
    private String userEmail;
    private LocalDate userBirth;
    private int userPoint;
    private boolean isAuth;
    private String userStatus;
    private LocalDateTime lastLoginAt;
    private String userGradeName;

    public ResponseUser(User user) {
        this.userNo = user.getUserNo();
        this.userId = user.getUserId();
        this.userPassword = user.getUserPassword();
        this.userName = user.getUserName();
        this.userPhoneNumber = user.getUserPhoneNumber();
        this.userEmail = user.getUserEmail();
        this.userBirth = user.getUserBirth();
        this.userPoint = user.getUserPoint();
        this.isAuth = user.isAuth();
        this.userStatus = user.getUserStatus().name();
        this.lastLoginAt = user.getLastLoginAt();
        this.userGradeName = user.getUserGrade().getGradeName().name();
    }

    public ResponseUser(Optional<User> user) {
        if (user.isPresent()) {
            User foundUser = user.get();
            this.userNo = foundUser.getUserNo();
            this.userId = foundUser.getUserId();
            this.userPassword = foundUser.getUserPassword();
            this.userName = foundUser.getUserName();
            this.userPhoneNumber = foundUser.getUserPhoneNumber();
            this.userEmail = foundUser.getUserEmail();
            this.userBirth = foundUser.getUserBirth();
            this.userPoint = foundUser.getUserPoint();
            this.isAuth = foundUser.isAuth();
            this.userStatus = foundUser.getUserStatus().name();
            this.lastLoginAt = foundUser.getLastLoginAt();
            this.userGradeName = foundUser.getUserGrade().getGradeName().name();
        }
        else {
            this.userNo = null;
            this.userId = null;
            this.userPassword = null;
            this.userName = null;
            this.userPhoneNumber = null;
            this.userEmail = null;
            this.userBirth = null;
            this.userPoint = 0;
            this.isAuth = false;
            this.userStatus = null;
            this.lastLoginAt = null;
            this.userGradeName = null;
        }
    }
}
