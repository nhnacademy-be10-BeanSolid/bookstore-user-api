package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.address.domain.QAddress;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.address.repository.queryfactory.impl.AddressRepositoryImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddressRepositoryTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<ResponseAddress> addressQuery;

    private AddressRepositoryImpl addressRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressRepository = new AddressRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("userId로 주소 목록 조회")
    void testFindAllByUserId() {
        String userId = "user123";
        QAddress address = QAddress.address;

        ResponseAddress responseAddress = new ResponseAddress(
                1L,
                "집",
                "경기도 성남시 분당구 대왕판교로 645번길 16",
                userId
        );

        when(queryFactory.select(Projections.constructor(ResponseAddress.class,
                address.addressId,
                address.addressNickName,
                address.addressDetail,
                address.user.userId)))
                .thenReturn(addressQuery);

        when(addressQuery.from(address)).thenReturn(addressQuery);
        when(addressQuery.where(address.user.userId.eq(userId))).thenReturn(addressQuery);
        when(addressQuery.fetch()).thenReturn(List.of(responseAddress));

        List<ResponseAddress> result = addressRepository.findAllByUserId(userId);

        assertThat(result)
                .isNotEmpty()
                .hasSize(1);
        assertThat(result.getFirst().getUserId()).isEqualTo(userId);
    }
}