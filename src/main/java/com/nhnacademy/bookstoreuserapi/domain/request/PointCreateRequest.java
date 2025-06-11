package com.nhnacademy.bookstoreuserapi.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointCreateRequest {

    private String userId;
    private Long typeId;
    private Long paymentId;
    private LocalDateTime earnedAndUsedAt;
    private Long earnedAndUsedPoint;
}
