package com.nhnacademy.bookstoreuserapi.guest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "비회원 생성 요청 DTO")
public record GuestCreateRequest (@Schema(description = "비회원 패스워드") @NotBlank @Size(max = 255) String guestPassword,
                                  @Schema(description = "주문ID") Long orderId
                                  ){
}
