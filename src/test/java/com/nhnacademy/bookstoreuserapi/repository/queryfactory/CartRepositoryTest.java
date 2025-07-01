package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.cart.domain.QCart;
import com.nhnacademy.bookstoreuserapi.cart.domain.ResponseCart;
import com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory.impl.CartRepositoryImpl;
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
    private JPAQuery<ResponseCart> cartQuery;

    @Mock
    private JPAQuery<Long> countQuery;

    private CartRepositoryImpl cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartRepository = new CartRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("userId로 장바구니 목록 조회")
    void testFindAllByUserId() {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);
        QCart cart = QCart.cart;

        ResponseCart responseCart = new ResponseCart(1L, 101L, userId, 3);

        when(queryFactory.select(Projections.constructor(ResponseCart.class,
                cart.cartId, cart.bookId, cart.user.userId, cart.quantity)))
                .thenReturn(cartQuery);
        when(cartQuery.from(cart)).thenReturn(cartQuery);
        when(cartQuery.where(cart.user.userId.eq(userId))).thenReturn(cartQuery);
        when(cartQuery.offset(pageable.getOffset())).thenReturn(cartQuery);
        when(cartQuery.limit(pageable.getPageSize())).thenReturn(cartQuery);
        when(cartQuery.fetch()).thenReturn(List.of(responseCart));

        when(queryFactory.select(cart.count())).thenReturn(countQuery);
        when(countQuery.from(cart)).thenReturn(countQuery);
        when(countQuery.where(cart.user.userId.eq(userId))).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(1L);

        Page<ResponseCart> result = cartRepository.findAllByUserId(userId, pageable);

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

        ResponseCart expectedCart = new ResponseCart(1L, bookId, userId, 2);

        when(queryFactory.select(Projections.constructor(ResponseCart.class,
                cart.cartId, cart.bookId, cart.user.userId, cart.quantity)))
                .thenReturn(cartQuery);
        when(cartQuery.from(cart)).thenReturn(cartQuery);
        when(cartQuery.where(cart.user.userId.eq(userId)
                .and(cart.bookId.eq(bookId))))
                .thenReturn(cartQuery);
        when(cartQuery.fetchOne()).thenReturn(expectedCart);

        ResponseCart result = cartRepository.findByUserIdAndBookId(userId, bookId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getBookId()).isEqualTo(bookId);
    }
}