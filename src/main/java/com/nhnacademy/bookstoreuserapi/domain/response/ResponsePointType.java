package com.nhnacademy.bookstoreuserapi.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "포인트 타입 응답 DTO")
public class ResponsePointType {
    @Schema(description = "포인트 타입 ID", example = "1")
    private Long typeId;
    @Schema(description = "포인트 타입 이름", example = "회원가입")
    private String typeName;
    @Schema(description = "획득 포인트", example = "5000")
    private Integer earningPoint;
    @Schema(description = "순수금액별 적립 비율")
    private Integer earningRate;
    @Schema(description = "유저 등급")
    private String gradeName;
}
