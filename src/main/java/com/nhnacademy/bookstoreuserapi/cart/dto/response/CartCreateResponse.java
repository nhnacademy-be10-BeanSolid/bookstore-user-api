package com.nhnacademy.bookstoreuserapi.cart.dto.response;

import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "장바구니 생성 응답 DTO")
public class CartCreateResponse {
    @Schema(description = "장바구니 ID", example = "1")
    private Long cartId;
    @Schema(description = "유저 ID", example = "test")
    private String userId;
    @Schema(description = "비회원 UUID")
    private String guestUUID;
    @Schema(description = "장바구니 소유자 타입", example = "USER")
    private OwnerType ownerType;
    @Schema(description = "장바구니 생성 시간", example = "2025-10-01 12:00:00")
    private LocalDateTime createdAt;
}
