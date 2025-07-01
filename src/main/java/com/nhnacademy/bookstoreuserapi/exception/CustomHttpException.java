package com.nhnacademy.bookstoreuserapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomHttpException extends RuntimeException {

    @Getter
    @RequiredArgsConstructor
    public enum CustomHttpStatus {

        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        METHOD_NOT_ALLOWED(405),
        NOT_ACCEPTABLE(406),
        REQUEST_TIMEOUT(408),
        CONFLICT(409),
        GONE(410),
        UNSUPPORTED_MEDIA_TYPE(415),
        UNPROCESSABLE_ENTITY(422),
        TOO_MANY_REQUESTS(429),

        INTERNAL_SERVER_ERROR(500);

        private final int code;
    }

    private final CustomHttpStatus CustomHttpStatus;
    private final String message;
}
