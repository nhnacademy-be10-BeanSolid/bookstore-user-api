package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Address;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.exception.*;
import com.nhnacademy.bookstoreuserapi.repository.AddressRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    // 주소 등록
    @Override
    public ResponseAddress save(AddressCreateRequest address) {
        long count = addressRepository.countByUser_UserId(address.getUserId());
        if (count >= 10) {
            throw new AddressLimitExceededException(address.getUserId());
        }
        if (address.getAddressDetail().length() > 255) {
            throw new AddressLengthExceededException();
        }
        boolean exists = addressRepository.existsByUser_UserIdAndAddressDetail(address.getUserId(), address.getAddressDetail());
        if (exists) {
            throw new AddressAlreadyExistException(address.getAddressDetail(), address.getUserId());
        }
        Address addressTmp = new Address();
        addressTmp.setAddressNickName(address.getAddressNickName());
        addressTmp.setAddressDetail(address.getAddressDetail());
        User user = userRepository.findById(address.getUserId())
                .orElseThrow(() -> new UserNotFoundException(address.getUserId()));
        addressTmp.setUser(user);
        Address savedAddress = addressRepository.save(addressTmp);
        return new ResponseAddress(
                savedAddress.getAddressId(),
                savedAddress.getAddressNickName(),
                savedAddress.getAddressDetail(),
                savedAddress.getUser().getUserId()
        );
    }

    // 주소 조회
    @Override
    public ResponseAddress getAddress(long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElse(null);
        if (findAddress == null) {
            throw new AddressNotFoundException(addressId);
        }
        return new ResponseAddress(
                findAddress.getAddressId(),
                findAddress.getAddressNickName(),
                findAddress.getAddressDetail(),
                findAddress.getUser().getUserId()
        );
    }

    // 주소 리스트 조회 (유저 ID로 조회)
    @Override
    public List<ResponseAddress> getAllAddresses(String userId) {
        List<Address> addresses = addressRepository.findAllByUser_UserId(userId);
        List<ResponseAddress> responseAddresses = new ArrayList<>();
        for (Address address : addresses) {
            responseAddresses.add(new ResponseAddress(
                    address.getAddressId(),
                    address.getAddressNickName(),
                    address.getAddressDetail(),
                    address.getUser().getUserId()
            ));
        }
        return responseAddresses;
    }

    // 주소 삭제
    @Override
    public void deleteAddress(long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElse(null);
        if(findAddress == null) {
            throw new AddressNotFoundException(addressId);
        }
        addressRepository.delete(findAddress);

    }

}
