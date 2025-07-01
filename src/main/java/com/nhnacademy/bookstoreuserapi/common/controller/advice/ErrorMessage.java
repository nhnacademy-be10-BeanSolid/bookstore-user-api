package com.nhnacademy.bookstoreuserapi.common.controller.advice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Schema(
        description = "에러 메시지 DTO",
        title = "에러 메시지",
        name = "ErrorMessage")
public class ErrorMessage {
    @Schema(description = "에러 발생 시간")
    private final String timeStamp;
    @Schema(description = "HTTP 상태 코드")
    private final int status;
    @Schema(description = "에러 메시지")
    private final String error;
    @Schema(description = "요청 경로")
    private final String path;
    @Schema(description = "에러 상세 메시지")
    private final String message;

    public ErrorMessage(int status, String error, String path, String message) {
        this.timeStamp = ZonedDateTime.now(ZoneOffset.UTC).toString();
        this.status = status;
        this.error = error;
        this.path = path;
        this.message = message;
    }
}
