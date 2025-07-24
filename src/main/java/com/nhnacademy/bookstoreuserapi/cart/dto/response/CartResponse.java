package com.nhnacademy.bookstoreuserapi.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "장바구니 응답 DTO")
public class CartResponse {
    @Schema(description = "장바구니 ID", example = "1")
    private long cartId;
    @Schema(description = "아이템 리스트")
    private List<CartItemDto> items;
}