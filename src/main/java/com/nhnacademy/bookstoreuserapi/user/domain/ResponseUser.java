package com.nhnacademy.bookstoreuserapi.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 응답 DTO")
public class ResponseUser {
    @Schema(description = "유저 번호") private Long userNo;
    @Schema(description = "유저 ID") private String userId;
    @Schema(description = "유저 패스워드") private String userPassword;
    @Schema(description = "유저 이름") private String userName;
    @Schema(description = "유저 전화번호") private String userPhoneNumber;
    @Schema(description = "유저 이메일") private String userEmail;
    @Schema(description = "유저 생일") private LocalDate userBirth;
    @Schema(description = "유저 포인트") private int userPoint;
    @Schema(description = "유저 권한", example = "false") private boolean isAuth;
    @Schema(description = "유저 상태") private String userStatus;
    @Schema(description = "유저 생성시각") private LocalDateTime createdAt;
    @Schema(description = "마지막 로그인 시각") private LocalDateTime lastLoginAt;
    @Schema(description = "유저 등급명") private String userGradeName;

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
        this.createdAt = user.getCreatedAt();
        this.lastLoginAt = user.getLastLoginAt();
        this.userGradeName = user.getUserGrade().getGradeName().name();
    }
}
