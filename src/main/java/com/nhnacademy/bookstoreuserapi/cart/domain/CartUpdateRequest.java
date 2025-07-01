package com.nhnacademy.bookstoreuserapi.cart.domain;


import jakarta.validation.constraints.Min;

public record CartUpdateRequest (@Min(0) int quantity){
}
