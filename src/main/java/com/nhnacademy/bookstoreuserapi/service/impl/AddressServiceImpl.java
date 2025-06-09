package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Address;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestAddress;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.exception.AddressAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.AddressLengthExceededException;
import com.nhnacademy.bookstoreuserapi.exception.AddressLimitExceededException;
import com.nhnacademy.bookstoreuserapi.exception.AddressNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl{
    private final AddressRepository addressRepository;


    // 주소 등록
    public ResponseAddress save(SignUpRequestAddress address) {
        long count = addressRepository.countByUserId(address.getUserId());
        if (count >= 10) {
            throw new AddressLimitExceededException(address.getUserId());
        }
        if (address.getAddressDetail().length() > 255) {
            throw new AddressLengthExceededException("주소는 255자 이내여야 합니다.");
        }
        boolean exists = addressRepository.existsByUserIdAndAddressDetail(address.getUserId(), address.getAddressDetail());
        if (exists) {
            throw new AddressAlreadyExistException(address.getAddressDetail(), address.getUserId());
        }
        Address addressTmp = new Address();
        addressTmp.setAddressNickName(address.getAddressNickName());
        addressTmp.setAddressDetail(address.getAddressDetail());
        addressTmp.setUserId(address.getUserId());
        Address savedAddress = addressRepository.save(addressTmp);
        return new ResponseAddress(
                savedAddress.getAddressId(),
                savedAddress.getAddressNickName(),
                savedAddress.getAddressDetail(),
                savedAddress.getUserId()
        );
    }

    // 주소 조회
    public ResponseAddress getAddress(long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElse(null);
        if (findAddress == null) {
            throw new AddressNotFoundException(addressId);
        }
        return new ResponseAddress(
                findAddress.getAddressId(),
                findAddress.getAddressNickName(),
                findAddress.getAddressDetail(),
                findAddress.getUserId()
        );
    }

    // 주소 리스트 조회 (유저 ID로 조회)
    public List<ResponseAddress> getAllAddresses(String userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        List<ResponseAddress> responseAddresses = new ArrayList<>();
        for (Address address : addresses) {
            responseAddresses.add(new ResponseAddress(
                    address.getAddressId(),
                    address.getAddressNickName(),
                    address.getAddressDetail(),
                    address.getUserId()
            ));
        }
        return responseAddresses;
    }

    // 주소 삭제
    public void deleteAddress(long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElse(null);
        if(findAddress == null) {
            throw new AddressNotFoundException(addressId);
        }
        addressRepository.delete(findAddress);

    }

}
