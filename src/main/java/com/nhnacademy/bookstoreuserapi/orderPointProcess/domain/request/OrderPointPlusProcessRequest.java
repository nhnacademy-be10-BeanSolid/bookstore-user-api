package com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderPointPlusProcessRequest(@NotNull Long orderNo,
                                       @Min(0) int purePrice) {
}
