package com.nhnacademy.bookstoreuserapi.domain.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestReview {
    private int evaluationScore;
    private String reviewContent;
    private String reviewPhoto;
    private ZonedDateTime reviewDate;
    private String userId;
    private long bookId;
}
