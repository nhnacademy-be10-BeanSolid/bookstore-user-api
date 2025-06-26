package com.nhnacademy.bookstoreuserapi.domain.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "유저 등급 수정 DTO")
public record UserGradeUpdateRequest (@Schema(description = "BASIC, ROYAL, GOLD, PLATINUM", example = "GOLD") @NotBlank @Size(max = 10) String gradeName,
                                      @Schema(description = "수정한 등급 달성 요구 금액", example = "100000") @Min(0) long requiredMoney){
}
