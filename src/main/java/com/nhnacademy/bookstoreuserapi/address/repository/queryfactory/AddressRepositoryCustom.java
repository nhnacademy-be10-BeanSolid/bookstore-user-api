package com.nhnacademy.bookstoreuserapi.address.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;

import java.util.List;

public interface AddressRepositoryCustom {

    List<ResponseAddress> findAllByUserId(String userId);
}
