package com.nhnacademy.bookstoreuserapi.cart.advice;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.controller.CartController;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

@ControllerAdvice(assignableTypes = CartController.class)
public class CartContextAdvice {
    @ModelAttribute
    public CartContext cartContext(
            @RequestHeader("X-OWNER-TYPE") OwnerType ownerType,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-GUEST-UUID", required = false) String guestUUID
    ) {
        return new CartContext(ownerType, userId, guestUUID);
    }
}
