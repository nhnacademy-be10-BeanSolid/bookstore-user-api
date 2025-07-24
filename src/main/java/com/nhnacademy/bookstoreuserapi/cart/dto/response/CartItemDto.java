package com.nhnacademy.bookstoreuserapi.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "장바구니 아이템 응답 DTO")
public class CartItemDto {
    @Schema(description = "아이템 ID", example = "1")
    private long itemId;
    @Schema(description = "수량", example = "2")
    private int quantity;

    public static CartItemDto from(com.nhnacademy.bookstoreuserapi.cart.domain.CartItem cartItem) {
        return CartItemDto.builder()
                .itemId(cartItem.getItemId())
                .quantity(cartItem.getQuantity())
                .build();
    }
}