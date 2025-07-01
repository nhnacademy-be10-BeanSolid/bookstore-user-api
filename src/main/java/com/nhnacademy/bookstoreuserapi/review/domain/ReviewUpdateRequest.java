package com.nhnacademy.bookstoreuserapi.review.domain;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequest (@Min(1) @Max(5) int evaluationScore,
                                   @NotBlank @Size(max = 255) String reviewContent,
                                   @Size(max = 255) String reviewPhoto){
}
