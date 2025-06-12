package com.nhnacademy.bookstoreuserapi.domain.response;

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
