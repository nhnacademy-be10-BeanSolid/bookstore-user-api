package com.nhnacademy.bookstoreuserapi.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "유저 생성 요청 DTO")
public record UserCreateRequest (@Schema(description = "유저 ID") @NotBlank @Size(max = 20) String userId,
                                 @Schema(description = "유저 패스워드") @NotBlank @Size(max = 255) String userPassword,
                                 @Schema(description = "유저 이름") @NotBlank @Size(max = 20) String userName,
                                 @Schema(description = "유저 전화번호") @NotBlank @Size(max = 15) String userPhoneNumber,
                                 @Schema(description = "유저 이메일") @NotBlank @Email @Size(max = 50) String userEmail,
                                 @Schema(description = "유저 생일") @NotNull @Past LocalDate userBirth){
}
