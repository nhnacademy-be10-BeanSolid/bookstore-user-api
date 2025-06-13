package com.nhnacademy.bookstoreuserapi.domain.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CartCreateRequest (@Min(1) long bookId,
                                 @NotBlank @Size(max = 20) String userId,
                                 @Min(1) int quantity){

}
