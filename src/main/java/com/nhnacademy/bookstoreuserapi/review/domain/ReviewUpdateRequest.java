package com.nhnacademy.bookstoreuserapi.review.domain;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ReviewUpdateRequest (@Min(1) @Max(5) int evaluationScore,
                                   @NotBlank @Size(max = 255) String reviewContent,
                                   List<@Size(max = 2083) String> imageUrls){
}
