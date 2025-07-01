package com.nhnacademy.bookstoreuserapi.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCart {
    private long cartId;
    private long bookId;
    private String userId;
    private int quantity;
}
