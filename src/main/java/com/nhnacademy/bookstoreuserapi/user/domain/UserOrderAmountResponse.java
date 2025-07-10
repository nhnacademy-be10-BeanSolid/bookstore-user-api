package com.nhnacademy.bookstoreuserapi.user.domain;

public record UserOrderAmountResponse(
    Long userNo,
    Long pureOrderAmount
) {
    public Long getpureOrderAmount() {
        return this.pureOrderAmount;
    }
}
