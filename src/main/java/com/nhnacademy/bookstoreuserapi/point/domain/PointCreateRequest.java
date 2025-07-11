package com.nhnacademy.bookstoreuserapi.point.domain;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PointCreateRequest (@NotBlank @Size(max = 20) String userId,
                                  @NotNull @Min(1) Long typeId,
                                  Long orderNo,
                                  @NotNull LocalDateTime earnedAndUsedAt,
                                  @NotBlank String earnedAndUsedPoint){

    public PointCreateRequest(String userId, Long typeId, Long orderNo, LocalDateTime earnedAndUsedAt, String earnedAndUsedPoint) {
        this.userId = userId;
        this.typeId = typeId;
        this.orderNo = orderNo;
        this.earnedAndUsedAt = earnedAndUsedAt;
        this.earnedAndUsedPoint = earnedAndUsedPoint;
    }
}
