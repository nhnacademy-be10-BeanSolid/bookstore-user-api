package com.nhnacademy.bookstoreuserapi.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Schema(description = "유저 정보 수정 요청 DTO")
public record UserUpdateRequest (
                                @Schema(description = "유저 패스워드")String userPassword,
                                @Schema(description = "유저 이름") String userName,
                                @Schema(description = "유저 전화번호") String userPhoneNumber,
                                @Schema(description = "유저 이메일") @Email String userEmail,
                                @Schema(description = "유저 생일") @CreatedDate LocalDate userBirth){
}
