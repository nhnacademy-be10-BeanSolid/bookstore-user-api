package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;

import java.util.List;

public interface AddressService {

    ResponseAddress save(String userId, AddressCreateRequest address);

    ResponseAddress getAddress(String userId, long addressId);

    List<ResponseAddress> getAllAddresses(String userId);

    void deleteAddress(String userId, long addressId);

}
