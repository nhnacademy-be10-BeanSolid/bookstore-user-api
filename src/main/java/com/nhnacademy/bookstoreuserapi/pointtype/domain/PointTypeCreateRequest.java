package com.nhnacademy.bookstoreuserapi.pointtype.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "포인트 타입 생성 요청 DTO")
public record PointTypeCreateRequest (@Schema(description = "타입 이름")@NotBlank @Size(max = 20) String typeName,
                                      @Schema(description = "포인트") @Min(0) int earningPoint,
                                      @Schema(description = "적립 비율") @Min(0) int earningRate,
                                      @Schema(description = "등급 명") @Size(max = 10) String gradeName,
                                      @Schema(description = "포인트 타입 활성화 여부") @NotNull Boolean isActive){
}
