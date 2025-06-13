package com.nhnacademy.bookstoreuserapi.domain.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateRequest {
    private int evaluationScore;
    private String reviewContent;
    private String reviewPhoto;
    private String userId;
    private long bookId;
}
