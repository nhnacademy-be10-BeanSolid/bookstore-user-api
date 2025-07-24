package com.nhnacademy.bookstoreuserapi.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 순수 주문 금액 응답 DTO")
public record UserOrderAmountResponse(
    @Schema(description = "유저 번호") Long userNo,
    @Schema(description = "순수주문금액") Long pureOrderAmount
) {
    public Long getpureOrderAmount() {
        return this.pureOrderAmount;
    }
}
