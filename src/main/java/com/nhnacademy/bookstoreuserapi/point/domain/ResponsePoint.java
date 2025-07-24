package com.nhnacademy.bookstoreuserapi.point.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "포인트 응답 DTO")
public class ResponsePoint {
    @Schema(description = "포인트 ID", example = "1")
    private Long pointId;
    @Schema(description = "유저 ID", example = "test")
    private String userId;
    @Schema(description = "포인트 타입 ID", example = "1")
    private Long typeId;
    @Schema(description = "주문 ID", example = "1")
    private Long orderId;
    @Schema(description = "적립 또는 사용 시각", example = "2025-10-01 12:00:00")
    private LocalDateTime earnedAndUsedAt;
    @Schema(description = "적립 또는 사용 포인트", example = "5000p 적립, 차감")
    private String earnedAndUsedPoint;
}
