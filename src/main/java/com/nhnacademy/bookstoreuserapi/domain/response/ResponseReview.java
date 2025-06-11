package com.nhnacademy.bookstoreuserapi.domain.response;


import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
