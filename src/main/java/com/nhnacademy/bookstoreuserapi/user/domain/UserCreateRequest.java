package com.nhnacademy.bookstoreuserapi.user.domain;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

//후에 다른 api와 필드명이 다르다?? -> @JsonProperty("바꿀필드명") 등으로 변경
public record UserCreateRequest (@NotBlank @Size(max = 20) String userId,
                                 @NotBlank @Size(max = 255) String userPassword,
                                 @NotBlank @Size(max = 20) String userName,
                                 @NotBlank @Size(max = 15) String userPhoneNumber,
                                 @NotBlank @Email @Size(max = 50) String userEmail,
                                 @NotNull @Past LocalDate userBirth){
}
