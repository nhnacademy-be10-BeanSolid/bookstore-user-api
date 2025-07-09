package com.nhnacademy.bookstoreuserapi.cart.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException(Long cartItemId) {
        super(String.format("장바구니 아이템(ID: %d)을 찾을 수 없습니다.", cartItemId));
    }
}
