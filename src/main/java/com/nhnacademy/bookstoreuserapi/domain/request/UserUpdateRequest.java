package com.nhnacademy.bookstoreuserapi.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

public record UserUpdateRequest (
                                String userPassword,
                                String userName,
                                String userPhoneNumber,
                                @Email String userEmail,
                                @CreatedDate LocalDate userBirth){
}
