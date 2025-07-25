package com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request;

import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "주문 포인트 적립 처리 요청 DTO")
public record OrderPointPlusProcessRequest(@Schema(description = "주문번호") @NotNull Long orderNo,
                                       @Schema(description = "포인트") @Min(0) int point,
                                       @Schema(description = "포인트 타입") @NotNull PointType pointType) {
}
