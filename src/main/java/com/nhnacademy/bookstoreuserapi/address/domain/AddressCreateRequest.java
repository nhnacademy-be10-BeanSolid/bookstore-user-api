package com.nhnacademy.bookstoreuserapi.address.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(description = "주소 생성 DTO")
public record AddressCreateRequest (
     @Schema(description = "주소 별칭") @NotBlank @Size(max = 30) String addressNickName,
     @Schema(description = "상세 주소") @NotBlank @Size(max = 255) String addressDetail,
     @Schema(description = "유저ID")@NotBlank @Size(max = 20) String userId){
}
