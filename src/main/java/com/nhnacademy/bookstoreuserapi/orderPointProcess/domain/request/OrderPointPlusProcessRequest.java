package com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request;

import jakarta.validation.constraints.NotNull;

public record OrderPointPlusProcessRequest(@NotNull Long orderNo,
                                       @NotNull int purePrice) {
}
