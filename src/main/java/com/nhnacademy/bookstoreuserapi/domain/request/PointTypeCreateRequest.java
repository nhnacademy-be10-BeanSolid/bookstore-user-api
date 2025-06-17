package com.nhnacademy.bookstoreuserapi.domain.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PointTypeCreateRequest (@NotBlank @Size(max = 20) String typeName,
                                      @Min(0) Long earningPoint,
                                      Integer earningRate,
                                      @Size(max = 10) String gradeName){
}
