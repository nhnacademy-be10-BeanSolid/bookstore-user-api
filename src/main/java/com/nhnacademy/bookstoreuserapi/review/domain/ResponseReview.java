package com.nhnacademy.bookstoreuserapi.review.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReview {

    private long reviewId;
    private int evaluationScore;
    private String reviewContent;
    private String reviewPhoto;
    private LocalDateTime reviewedAt;
    private LocalDateTime updatedAt;
    private String userId;
    private long bookId;
}
