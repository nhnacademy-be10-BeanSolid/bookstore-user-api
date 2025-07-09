package com.nhnacademy.bookstoreuserapi.cart.repository;

import com.nhnacademy.bookstoreuserapi.cart.domain.QCart;
import com.nhnacademy.bookstoreuserapi.cart.domain.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory.impl.CartItemRepositoryImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartRepositoryTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<CartResponse> cartQuery;

    @Mock
    private JPAQuery<Long> countQuery;

    private CartItemRepositoryImpl cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartRepository = new CartItemRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("userId로 장바구니 목록 조회")
    void testFindAllByUserId() {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);
        QCart cart = QCart.cart;

        CartResponse cartResponse = new CartResponse(1L, 101L, userId, 3);

        when(queryFactory.select(Projections.constructor(CartResponse.class,
                cart.cartId, cart.bookId, cart.user.userId, cart.quantity)))
                .thenReturn(cartQuery);
        when(cartQuery.from(cart)).thenReturn(cartQuery);
        when(cartQuery.where(cart.user.userId.eq(userId))).thenReturn(cartQuery);
        when(cartQuery.offset(pageable.getOffset())).thenReturn(cartQuery);
        when(cartQuery.limit(pageable.getPageSize())).thenReturn(cartQuery);
        when(cartQuery.fetch()).thenReturn(List.of(cartResponse));

        when(queryFactory.select(cart.count())).thenReturn(countQuery);
        when(countQuery.from(cart)).thenReturn(countQuery);
        when(countQuery.where(cart.user.userId.eq(userId))).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(1L);

        Page<CartResponse> result = cartRepository.findAllByUserId(userId, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("userId + bookId로 장바구니 조회")
    void testFindByUserIdAndBookId() {
        String userId = "user123";
        long bookId = 101L;
        QCart cart = QCart.cart;

        CartResponse expectedCart = new CartResponse(1L, bookId, userId, 2);

        when(queryFactory.select(Projections.constructor(CartResponse.class,
                cart.cartId, cart.bookId, cart.user.userId, cart.quantity)))
                .thenReturn(cartQuery);
        when(cartQuery.from(cart)).thenReturn(cartQuery);
        when(cartQuery.where(cart.user.userId.eq(userId)
                .and(cart.bookId.eq(bookId))))
                .thenReturn(cartQuery);
        when(cartQuery.fetchOne()).thenReturn(expectedCart);

        CartResponse result = cartRepository.findByUserIdAndBookId(userId, bookId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getBookId()).isEqualTo(bookId);
    }
}