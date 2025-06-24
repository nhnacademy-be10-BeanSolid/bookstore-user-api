package com.nhnacademy.bookstoreuserapi.domain.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 등급 응답 DTO")
public class ResponseUserGrade {
    @Schema(description = "등급 이름", example = "GOLD")
    private String gradeName;
    @Schema(description = "등급 달성 요구 금액", example = "100000")
    private long requiredMoney;

}
