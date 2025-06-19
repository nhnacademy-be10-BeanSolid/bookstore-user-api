package com.nhnacademy.bookstoreuserapi.domain.request;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PointCreateRequest (@NotBlank @Size(max = 20) String userId,
                                  @NotNull @Min(1) Long typeId,
                                  @Min(1) Long paymentId,
                                  @NotNull LocalDateTime earnedAndUsedAt,
                                  @NotNull int earnedAndUsedPoint){
}
