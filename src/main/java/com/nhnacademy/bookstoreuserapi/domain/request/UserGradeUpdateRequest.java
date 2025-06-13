package com.nhnacademy.bookstoreuserapi.domain.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserGradeUpdateRequest (@NotBlank @Size(max = 10) String gradeName,
                                      @Min(0) long requiredMoney){
}
