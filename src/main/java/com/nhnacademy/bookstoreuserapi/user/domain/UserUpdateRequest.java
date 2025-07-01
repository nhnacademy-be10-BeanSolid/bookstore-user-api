package com.nhnacademy.bookstoreuserapi.user.domain;

import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

public record UserUpdateRequest (
                                String userPassword,
                                String userName,
                                String userPhoneNumber,
                                @Email String userEmail,
                                @CreatedDate LocalDate userBirth){
}
