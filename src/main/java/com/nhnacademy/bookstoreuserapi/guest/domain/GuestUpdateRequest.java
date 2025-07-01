package com.nhnacademy.bookstoreuserapi.guest.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GuestUpdateRequest (@NotBlank @Size(max = 255) String guestPassword,
                                  @NotBlank @Size(max = 20) String guestName,
                                  @NotBlank @Size(max = 15) String guestPhoneNumber,
                                  @NotBlank @Size(max = 255) String guestAddress
                                  ){
}
