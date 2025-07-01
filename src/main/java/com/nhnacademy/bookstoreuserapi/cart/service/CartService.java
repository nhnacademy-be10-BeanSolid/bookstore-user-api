package com.nhnacademy.bookstoreuserapi.cart.service;

import com.nhnacademy.bookstoreuserapi.cart.domain.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.ResponseCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CartService {

    ResponseCart addCart(String userId, CartCreateRequest cart);

    Optional<ResponseCart> editCart(String userId, long cartId, CartUpdateRequest cart);

    ResponseCart getCart(String userId, long cartId);

    Page<ResponseCart> getCartsByUserId(String userId, Pageable pageable);

    void deleteCart(String userId, long cartId);

    void deleteCartsByUserId(String userId);

}
