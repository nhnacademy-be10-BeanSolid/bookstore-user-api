package com.nhnacademy.bookstoreuserapi.address.service;

import com.nhnacademy.bookstoreuserapi.address.domain.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;

import java.util.List;

public interface AddressService {

    ResponseAddress save(String userId, AddressCreateRequest address);

    ResponseAddress getAddress(String userId, long addressId);

    List<ResponseAddress> getAllAddresses(String userId);

    void deleteAddress(String userId, long addressId);

}
