package com.nhnacademy.bookstoreuserapi.domain.response;


import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReview {

    private long reviewId;
    private int evaluationScore;
    private String reviewContent;
    private String reviewPhoto;
    private ZonedDateTime reviewedAt;
    private ZonedDateTime updatedAt;
    private String userId;
    private long bookId;
}
