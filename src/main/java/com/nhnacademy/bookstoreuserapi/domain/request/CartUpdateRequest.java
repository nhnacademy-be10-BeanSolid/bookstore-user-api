package com.nhnacademy.bookstoreuserapi.domain.request;


import jakarta.validation.constraints.Min;

public record CartUpdateRequest (@Min(0) int quantity){
}
