package com.nhnacademy.bookstoreuserapi.review.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReview {

    private long reviewId;
    private int evaluationScore;
    private String reviewContent;
    private List<String> reviewImages;
    private LocalDateTime reviewedAt;
    private LocalDateTime updatedAt;
    private String userId;
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
