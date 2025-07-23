package com.nhnacademy.bookstoreuserapi.review.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "리뷰 생성 요청 DTO")
public record ReviewCreateRequest (@Schema(description = "평가 점수(1~5)") @Min(1) @Max(5) int evaluationScore,
                                   @Schema(description = "리뷰 내용") @NotBlank @Size(max = 255) String reviewContent,
                                   @Schema(description = "이미지 URL") List<@Size(max = 2083) String> imageUrls,
                                   @Schema(description = "유저ID") @NotBlank @Size(max = 20) String userId,
                                   @Schema(description = "책ID") @Min(1) long bookId){
}
