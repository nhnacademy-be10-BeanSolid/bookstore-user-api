package com.nhnacademy.bookstoreuserapi.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

public record UserUpdateRequest (@NotBlank String userId,
                                @NotBlank String userPassword,
                                @NotBlank String userName,
                                @NotBlank String userPhoneNumber,
                                @Email String userEmail,
                                @CreatedDate LocalDate userBirth){
}
