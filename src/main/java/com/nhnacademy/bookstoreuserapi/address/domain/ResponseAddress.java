package com.nhnacademy.bookstoreuserapi.address.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주소 응답 DTO")
public class ResponseAddress {
    @Schema(description = "주소 ID", example = "1")
    private long addressId;
    @Schema(description = "주소 별칭", example = "집")
    private String addressNickName;
    @Schema(description = "상세 주소", example = "서울시 강남구 역삼동 123-45")
    private String addressDetail;
    @Schema(description = "유저 ID", example = "test")
    private String userId;
}
