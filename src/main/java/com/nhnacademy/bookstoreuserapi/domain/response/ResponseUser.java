package com.nhnacademy.bookstoreuserapi.domain.response;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUser {
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
}
