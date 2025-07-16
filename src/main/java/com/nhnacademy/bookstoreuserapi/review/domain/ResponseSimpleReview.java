package com.nhnacademy.bookstoreuserapi.review.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseSimpleReview {
    private long reviewId;
    private int evaluationScore;
    private String reviewContent;
    private LocalDateTime reviewedAt;
    private LocalDateTime updatedAt;
    private String userId;
    private long bookId;

    public ResponseSimpleReview(long reviewId, int evaluationScore, String reviewContent,
                                LocalDateTime reviewedAt, LocalDateTime updatedAt,
                                String userId, long bookId) {
        this.reviewId = reviewId;
        this.evaluationScore = evaluationScore;
        this.reviewContent = reviewContent;
        this.reviewedAt = reviewedAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.bookId = bookId;
    }
}