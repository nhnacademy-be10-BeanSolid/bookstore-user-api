package com.nhnacademy.bookstoreuserapi.pointtype.domain;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PointTypeCreateRequest (@NotBlank @Size(max = 20) String typeName,
                                      @Min(0) int earningPoint,
                                      int earningRate,
                                      @Size(max = 10) String gradeName,
                                      @NotNull boolean isActive){
}
