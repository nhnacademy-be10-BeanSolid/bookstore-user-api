package com.nhnacademy.bookstoreuserapi.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Schema(description = "장바구니 아이템 추가 요청 DTO")
public record CartAddItemRequest(
        @Schema(description = "아이템 ID")
        @NotNull long itemId,
        @Schema(description = "수량")
        @Min(1) int quantity
) {
}
