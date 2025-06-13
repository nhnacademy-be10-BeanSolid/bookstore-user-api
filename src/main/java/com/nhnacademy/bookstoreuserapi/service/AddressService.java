package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;

import java.util.List;

public interface AddressService {

    ResponseAddress save(AddressCreateRequest address);

    ResponseAddress getAddress(long addressId);

    List<ResponseAddress> getAllAddresses(String userId);

    void deleteAddress(long addressId);

}
