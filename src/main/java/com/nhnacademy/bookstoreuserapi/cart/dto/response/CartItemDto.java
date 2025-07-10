package com.nhnacademy.bookstoreuserapi.cart.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {
    private long itemId;
    private int quantity;

    public static CartItemDto from(com.nhnacademy.bookstoreuserapi.cart.domain.CartItem cartItem) {
        return CartItemDto.builder()
                .itemId(cartItem.getItemId())
                .quantity(cartItem.getQuantity())
                .build();
    }
}