package com.nhnacademy.bookstoreuserapi.domain.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "포인트 등급 생성 DTO")
public record PointTypeCreateRequest (@Schema(description = "회원가입, 리뷰작성, 순수금액별 포인트적립 등", example = "회원가입") @NotBlank @Size(max = 20) String typeName,
                                      @Schema(description = "5000, 500 등", example = "5000")@Min(0) int earningPoint,
                                      @Schema(description = "순수 금액별 포인트 적립률") int earningRate,
                                      @Schema(description = "유저 등급(BASIC, GOLD 등)")@Size(max = 10) String gradeName){
}
