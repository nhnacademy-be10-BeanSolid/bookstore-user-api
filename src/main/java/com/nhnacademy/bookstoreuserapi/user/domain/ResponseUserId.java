package com.nhnacademy.bookstoreuserapi.user.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 아이디 응답 DTO")
public class ResponseUserId {
    @Schema(description = "유저 ID") private String userId;
    @Schema(description = "유저 번호") private Long userNo;

    public ResponseUserId(User user) {
        this.userId = user.getUserId();
    }
}
