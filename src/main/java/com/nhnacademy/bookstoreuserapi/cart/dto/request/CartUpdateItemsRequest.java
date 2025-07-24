package com.nhnacademy.bookstoreuserapi.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "장바구니 아이템 업데이트 요청 DTO")
public record CartUpdateItemsRequest(
        @Schema(description = "업데이트할 아이템 목록")
        @NotNull
        @Valid
        List<CartItemUpdate> items
) {
    @Schema(description = "장바구니 아이템 업데이트 DTO")
    public record CartItemUpdate(
            @Schema(description = "책 ID")
            @NotNull Long bookId,
            @Schema(description = "수량")
            @Min(1) int quantity
    ) {}
}
