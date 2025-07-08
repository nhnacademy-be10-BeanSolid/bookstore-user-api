package com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory.impl;

import com.nhnacademy.bookstoreuserapi.cart.domain.QCartItem;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartItemDto;
import com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory.CartRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CartItemDto> findAllByCartId(Long cartId) {
        QCartItem cartItem = QCartItem.cartItem;

        return queryFactory
                .select(Projections.constructor(CartItemDto.class,
                        cartItem.bookId,
                        cartItem.quantity))
                .from(cartItem)
                .where(cartItem.cart.cartId.eq(cartId))
                .fetch();
    }
}