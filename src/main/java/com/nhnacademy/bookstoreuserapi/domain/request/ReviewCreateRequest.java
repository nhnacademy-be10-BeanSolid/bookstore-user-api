package com.nhnacademy.bookstoreuserapi.domain.request;


import jakarta.validation.constraints.*;

public record ReviewCreateRequest (@Min(1) @Max(5) int evaluationScore,
                                   @NotBlank @Size(max = 255) String reviewContent,
                                   @Size(max = 255) String reviewPhoto,
                                   @NotBlank @Size(max = 20) String userId,
                                   @Min(1) long bookId){

}
