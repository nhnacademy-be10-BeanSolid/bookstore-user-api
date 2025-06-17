package com.nhnacademy.bookstoreuserapi.repository.queryfactory.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.QCart;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.CartRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<ResponseCart> findAllByUserId(String userId, Pageable pageable) {
        QCart cart = QCart.cart;

        List<ResponseCart> cartList = queryFactory
                .select(Projections.constructor(ResponseCart.class,
                        cart.cartId,
                        cart.bookId,
                        cart.user.userId,
                        cart.quantity))
                .from(cart)
                .where(cart.user.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(cart.count())
                .from(cart)
                .where(cart.user.userId.eq(userId))
                .fetchOne();
        return new PageImpl<>(cartList, pageable, total == null ? 0 : total);
    }
    @Override
    public ResponseCart findByUserIdAndBookId(String userId, long bookId){
        QCart cart = QCart.cart;
        return queryFactory
                .select(Projections.constructor(ResponseCart.class,
                        cart.cartId,
                        cart.bookId,
                        cart.user.userId,
                        cart.quantity))
                .from(cart)
                .where(cart.user.userId.eq(userId)
                        .and(cart.bookId.eq(bookId)))
                        .fetchOne();
    }
}
