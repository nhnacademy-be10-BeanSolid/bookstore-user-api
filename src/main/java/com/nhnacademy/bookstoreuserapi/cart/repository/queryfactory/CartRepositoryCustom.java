package com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartRepositoryCustom {
    List<CartItemDto> findAllByCartId(Long cartId);
}