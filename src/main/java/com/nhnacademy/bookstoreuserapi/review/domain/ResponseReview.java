package com.nhnacademy.bookstoreuserapi.review.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 응답 DTO")
public class ResponseReview {

    @Schema(description = "리뷰 ID", example = "1")
    private long reviewId;
    @Schema(description = "평가 점수(1~5)", example = "5")
    private int evaluationScore;
    @Schema(description = "리뷰 내용", example = "이 책 정말 좋았어요!")
    private String reviewContent;
    @Schema(description = "리뷰 이미지 URL 목록", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private List<String> reviewImages;
    @Schema(description = "리뷰 작성 시간", example = "2025-10-01 12:00:00")
    private LocalDateTime reviewedAt;
    @Schema(description = "리뷰 수정 시간", example = "2025-10-01 12:30:00")
    private LocalDateTime updatedAt;
    @Schema(description = "작성자 ID", example = "test")
    private String userId;
    @Schema(description = "책 ID", example = "1")
    private long bookId;

    public static ResponseReview from(Review review) {
        List<String> imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getImageUrl)
                .toList();

        return new ResponseReview(
                review.getReviewId(),
                review.getEvaluationScore(),
                review.getReviewContent(),
                imageUrls,
                review.getReviewedAt(),
                review.getUpdatedAt(),
                review.getUser().getUserId(), // 필요 시 null 체크
                review.getBookId()
        );
    }

}
