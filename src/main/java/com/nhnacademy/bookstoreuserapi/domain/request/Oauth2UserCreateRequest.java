package com.nhnacademy.bookstoreuserapi.domain.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record Oauth2UserCreateRequest(@NotBlank @Size(max = 20) String provider,
                                      @NotBlank String providerId,
                                      @Size(max = 20) String userName,
                                      @Size(max = 15) String userPhoneNumber,
                                      @Email @Size(max = 50) String userEmail,
                                      @Email @Size(max = 50) String userMaskedEmail,
                                      @Past LocalDate userBirth){
}
