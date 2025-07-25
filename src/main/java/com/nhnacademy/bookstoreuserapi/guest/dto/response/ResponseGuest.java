package com.nhnacademy.bookstoreuserapi.guest.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비회원 응답 DTO")
public class ResponseGuest {
    @Schema(description = "비회원ID") private Long guestId;
    @Schema(description = "주문ID") private Long orderId;
}
