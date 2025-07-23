package com.nhnacademy.bookstoreuserapi.review.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(description = "간단한 리뷰 응답 DTO - 사용자별 리뷰 조회에 사용")
public class ResponseSimpleReviewByUser {
    @Schema(description = "리뷰 ID", example = "1")
    private long reviewId;
    @Schema(description = "평가 점수(1~5)", example = "5")
    private int evaluationScore;
    @Schema(description = "리뷰 내용", example = "이 책 정말 좋았어요!")
    private String reviewContent;
    @Schema(description = "리뷰 작성 시간", example = "2025-10-01 12:00:00")
    private LocalDateTime reviewedAt;
    @Schema(description = "리뷰 수정 시간", example = "2025-10-01 12:30:00")
    private LocalDateTime updatedAt;
    @Schema(description = "작성자 ID", example = "test")
    private String userId;
    @Schema(description = "책 ID", example = "1")
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