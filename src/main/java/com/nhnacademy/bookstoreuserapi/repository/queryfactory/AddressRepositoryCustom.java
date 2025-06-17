package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;

import java.util.List;

public interface AddressRepositoryCustom {

    List<ResponseAddress> findAllByUserId(String userId);
}
