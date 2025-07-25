package com.nhnacademy.bookstoreuserapi.point.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


@Schema(description = "포인트 생성 요청 DTO")
public record PointCreateRequest (@Schema(description = "유저ID") @NotBlank @Size(max = 20) String userId,
                                  @Schema(description = "타입ID") @NotNull @Min(1) Long typeId,
                                  @Schema(description = "주문ID") Long orderId,
                                  @Schema(description = "적립 또는 사용 시각", example = "2025-10-01 12:00:00") @NotNull LocalDateTime earnedAndUsedAt,
                                  @Schema(description = "적립유형") @NotBlank String earnedAndUsedPoint){
}
