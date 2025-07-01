package com.nhnacademy.bookstoreuserapi.address.repository.queryfactory.impl;

import com.nhnacademy.bookstoreuserapi.address.repository.queryfactory.AddressRepositoryCustom;
import com.nhnacademy.bookstoreuserapi.domain.entity.QAddress;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


import java.util.List;

@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<ResponseAddress> findAllByUserId(String userId) {
        QAddress address = QAddress.address;

        return queryFactory
                .select(Projections.constructor(ResponseAddress.class,
                        address.addressId,
                        address.addressNickName,
                        address.addressDetail,
                        address.user.userId))
                .from(address)
                .where(address.user.userId.eq(userId))
                .fetch();
    }
}
