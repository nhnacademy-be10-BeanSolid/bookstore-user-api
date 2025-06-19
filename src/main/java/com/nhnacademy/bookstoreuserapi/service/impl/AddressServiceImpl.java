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

import java.util.List;

import static com.nhnacademy.bookstoreuserapi.util.OwnerShipValidator.validate;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    // 주소 등록
    @Override
    public ResponseAddress save(String userId, AddressCreateRequest address) {
        validate(userId, address.userId());
        long count = addressRepository.countByUser_UserId(address.userId());
        if (count >= 10) {
            throw new AddressLimitExceededException(address.userId());
        }
        boolean exists = addressRepository.existsByUser_UserIdAndAddressDetail(address.userId(), address.addressDetail());
        if (exists) {
            throw new AddressAlreadyExistException(address.addressDetail(), address.userId());
        }
        Address addressTmp = new Address();
        addressTmp.setAddressNickName(address.addressNickName());
        addressTmp.setAddressDetail(address.addressDetail());
        User user = userRepository.findById(address.userId())
                .orElseThrow(() -> new UserNotFoundException(address.userId()));
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
    public ResponseAddress getAddress(String userId, long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElse(null);
        if (findAddress == null) {
            throw new AddressNotFoundException(addressId);
        }
        validate(userId, findAddress.getUser().getUserId());

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
        return addressRepository.findAllByUserId(userId);
    }

    // 주소 삭제
    @Override
    public void deleteAddress(String userId, long addressId) {
        Address findAddress = addressRepository.findById(addressId).orElse(null);
        if(findAddress == null) {
            throw new AddressNotFoundException(addressId);
        }
        validate(userId, findAddress.getUser().getUserId());

        addressRepository.delete(findAddress);

    }

}
