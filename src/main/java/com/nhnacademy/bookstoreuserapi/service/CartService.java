package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CartService {

    ResponseCart addCart(CartCreateRequest cart);

    Optional<ResponseCart> editCart(long cartId, CartUpdateRequest cart);

    ResponseCart getCart(long cartId);

    Page<ResponseCart> getCartsByUserId(String userId, Pageable pageable);

    void deleteCart(long cartId);

    void deleteCartsByUserId(String userId);

}
