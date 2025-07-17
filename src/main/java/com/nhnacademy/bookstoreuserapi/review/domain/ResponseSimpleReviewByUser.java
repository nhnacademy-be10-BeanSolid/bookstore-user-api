package com.nhnacademy.bookstoreuserapi.review.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ResponseSimpleReviewByUser {
    private long reviewId;
    private int evaluationScore;
    private String reviewContent;
    private LocalDateTime reviewedAt;
    private LocalDateTime updatedAt;
    private String userId;
    private long bookId;

    @JsonCreator
    public ResponseSimpleReviewByUser(
            @JsonProperty("reviewId") long reviewId,
            @JsonProperty("evaluationScore") int evaluationScore,
            @JsonProperty("reviewContent") String reviewContent,
            @JsonProperty("reviewedAt") LocalDateTime reviewedAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt,
            @JsonProperty("userId") String userId,
            @JsonProperty("bookId") long bookId) {
        this.reviewId = reviewId;
        this.evaluationScore = evaluationScore;
        this.reviewContent = reviewContent;
        this.reviewedAt = reviewedAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.bookId = bookId;
    }
}