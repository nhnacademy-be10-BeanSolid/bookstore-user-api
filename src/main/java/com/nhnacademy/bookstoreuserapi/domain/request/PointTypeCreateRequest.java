package com.nhnacademy.bookstoreuserapi.domain.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PointTypeCreateRequest (@NotBlank @Size(max = 20) String typeName,
                                      @NotNull @Min(0) Long earningPoint,
                                      @Min(1) Integer earningRate,
                                      @Size(max = 10) String gradeName){
}
