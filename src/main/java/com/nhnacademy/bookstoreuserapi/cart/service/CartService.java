package com.nhnacademy.bookstoreuserapi.cart.service;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    CartCreateResponse createCart(CartContext context);
    CartResponse getCart(CartContext context);
    CartResponse addItem(CartContext context, CartAddItemRequest request);
    CartResponse updateItem(CartContext context, Long itemId, CartUpdateRequest request);
    CartResponse deleteItem(CartContext context, Long itemId);
    CartResponse deleteItems(CartContext context, List<Long> itemIds);
}
