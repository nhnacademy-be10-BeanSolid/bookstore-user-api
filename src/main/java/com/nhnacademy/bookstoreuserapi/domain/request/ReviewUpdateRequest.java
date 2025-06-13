package com.nhnacademy.bookstoreuserapi.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequest {
    private int evaluationScore;
    private String reviewContent;
    private String reviewPhoto;
}
