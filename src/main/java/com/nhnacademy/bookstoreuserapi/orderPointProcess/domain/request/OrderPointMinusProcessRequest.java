package com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderPointMinusProcessRequest (@NotNull Long orderNo,
                                            @Min(0) int usePoint) {
}