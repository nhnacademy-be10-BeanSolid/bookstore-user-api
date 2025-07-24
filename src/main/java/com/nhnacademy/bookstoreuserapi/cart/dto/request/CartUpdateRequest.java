package com.nhnacademy.bookstoreuserapi.cart.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "장바구니 업데이트 요청 DTO")
public record CartUpdateRequest (@Schema(description = "수량") @Min(1) int quantity){
}
