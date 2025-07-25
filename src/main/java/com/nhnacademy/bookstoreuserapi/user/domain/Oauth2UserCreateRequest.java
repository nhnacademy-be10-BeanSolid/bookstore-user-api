package com.nhnacademy.bookstoreuserapi.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "OAuth2 유저 생성 요청 DTO")
public record Oauth2UserCreateRequest(@Schema(description = "OAuth2제공사") @NotBlank @Size(max = 20) String provider,
                                      @Schema(description = "제공사ID") @NotBlank String providerId,
                                      @Schema(description = "유저 이름") @Size(max = 20) String userName,
                                      @Schema(description = "유저 전화번호") @Size(max = 15) String userPhoneNumber,
                                      @Schema(description = "유저 이메일") @Email @Size(max = 50) String userEmail,
                                      @Schema(description = "유저 생일") @Past LocalDate userBirth){
}
