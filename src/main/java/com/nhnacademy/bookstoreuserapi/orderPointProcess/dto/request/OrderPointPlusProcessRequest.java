package com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.request;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.PointType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderPointPlusProcessRequest(@NotNull Long orderNo,
                                       @Min(0) int point,
                                       @NotNull PointType pointType) {
}
