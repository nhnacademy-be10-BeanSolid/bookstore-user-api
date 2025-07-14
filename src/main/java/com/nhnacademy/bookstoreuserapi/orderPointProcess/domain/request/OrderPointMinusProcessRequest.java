package com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request;

import jakarta.validation.constraints.NotNull;

public record OrderPointMinusProcessRequest (@NotNull Long orderNo,
                                            @NotNull int usePoint) {
}