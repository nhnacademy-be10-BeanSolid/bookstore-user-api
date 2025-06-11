package com.nhnacademy.bookstoreuserapi.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePoint {

    private Long pointId;
    private String userId;
    private Long typeId;
    private Long paymentId;
    private LocalDateTime earnedAndUsedAt;
    private Long earnedAndUsedPoint;
}
