package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Address;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestAddress;
import com.nhnacademy.bookstoreuserapi.exception.AddressAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.AddressLengthExceededException;
import com.nhnacademy.bookstoreuserapi.exception.AddressLimitExceededException;
import com.nhnacademy.bookstoreuserapi.exception.AddressNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.AddressRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    AddressRepository addressRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserGradeRepository userGradeRepository;

    @InjectMocks
    AddressServiceImpl addressService;

    @Test
    void saveAddress() {
        SignUpRequestAddress signUpRequestAddress = new SignUpRequestAddress("Home", "123 Main St", "user123");
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        Address address = new Address(0L, "Home", "123 Main St", new User(
                "test",
                "plainPassword",
                "김철수",
                "01098765432",
                "kim@test.com",
                LocalDate.of(1995, 6, 15),
                0,
                false,
                User.Status.ACTIVE,
                LocalDateTime.now(),
                0,
                userGrade
        ));

        Mockito.when(addressRepository.countByUser_UserId(signUpRequestAddress.getUserId())).thenReturn(1L);
        Mockito.when(addressRepository.existsByUser_UserIdAndAddressDetail(signUpRequestAddress.getUserId(), signUpRequestAddress.getAddressDetail())).thenReturn(false);
        Mockito.when(addressRepository.save(address)).thenReturn(address);
        Mockito.when(userRepository.findById(signUpRequestAddress.getUserId())).thenReturn(Optional.of(address.getUser()));

        addressService.save(signUpRequestAddress);
        Mockito.verify(addressRepository, Mockito.times(1)).countByUser_UserId(signUpRequestAddress.getUserId());
        Mockito.verify(addressRepository, Mockito.times(1)).existsByUser_UserIdAndAddressDetail(signUpRequestAddress.getUserId(), signUpRequestAddress.getAddressDetail());
        Mockito.verify(addressRepository, Mockito.times(1)).save(address);
        Mockito.verify(userRepository, Mockito.times(1)).findById(signUpRequestAddress.getUserId());
    }

    @Test
    void saveAddressFailAlreadyExists() {
        SignUpRequestAddress signUpRequestAddress = new SignUpRequestAddress("Home", "123 Main St", "user123");

        Mockito.when(addressRepository.existsByUser_UserIdAndAddressDetail(signUpRequestAddress.getUserId(), signUpRequestAddress.getAddressDetail())).thenReturn(true);

        try {
            addressService.save(signUpRequestAddress);
        } catch (Exception e) {
            assert e instanceof AddressAlreadyExistException;
        }

        Mockito.verify(addressRepository, Mockito.times(1)).existsByUser_UserIdAndAddressDetail(signUpRequestAddress.getUserId(), signUpRequestAddress.getAddressDetail());
    }

    @Test
    void saveAddressFailLimitExceeded() {
        SignUpRequestAddress signUpRequestAddress = new SignUpRequestAddress("Home", "123 Main St", "user123");
        Mockito.when(addressRepository.countByUser_UserId(signUpRequestAddress.getUserId())).thenReturn(10L);
        try {
            addressService.save(signUpRequestAddress);
        } catch (Exception e) {
            assert e instanceof AddressLimitExceededException;
        }
        Mockito.verify(addressRepository, Mockito.times(1)).countByUser_UserId(signUpRequestAddress.getUserId());
    }

    @Test
    void saveAddressFailLengthExceeded() {
        SignUpRequestAddress signUpRequestAddress = new SignUpRequestAddress("Home", "A".repeat(256), "user123");
        try {
            addressService.save(signUpRequestAddress);
        } catch (Exception e) {
            assert e instanceof AddressLengthExceededException;
        }
        Mockito.verify(addressRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAddress() {
        long addressId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        Address address = new Address(addressId, "Home", "123 Main St", new User(
                "test",
                "plainPassword",
                "김철수",
                "01098765432",
                "kim@test.com",
                LocalDate.of(1995, 6, 15),
                0,
                false,
                User.Status.ACTIVE,
                LocalDateTime.now(),
                0,
                userGrade
        ));

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.getAddress(addressId);
        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
    }

    @Test
    void getAddressFailNotFound() {
        long addressId = 1L;

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        try {
            addressService.getAddress(addressId);
        } catch (Exception e) {
            assert e instanceof AddressNotFoundException;
        }

        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
    }

    @Test
    void getAllAddresses() {
        String userId = "user123";
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        Address address = new Address(1L, "Home", "123 Main St", new User(
                userId,
                "plainPassword",
                "김철수",
                "01098765432",
                "kim@test.com",
                LocalDate.of(1995, 6, 15),
                0,
                false,
                User.Status.ACTIVE,
                LocalDateTime.now(),
                0,
                userGrade
        ));

        Mockito.when(addressRepository.findAllByUser_UserId(userId)).thenReturn(List.of(address));

        addressService.getAllAddresses(userId);
        Mockito.verify(addressRepository, Mockito.times(1)).findAllByUser_UserId(userId);
    }

    @Test
    void deleteAddress() {
        long addressId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        Address address = new Address(addressId, "Home", "123 Main St", new User(
                "test",
                "plainPassword",
                "김철수",
                "01098765432",
                "kim@test.com",
                LocalDate.of(1995, 6, 15),
                0,
                false,
                User.Status.ACTIVE,
                LocalDateTime.now(),
                0,
                userGrade
        ));

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        Mockito.doNothing().when(addressRepository).delete(address);

        addressService.deleteAddress(addressId);
        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
        Mockito.verify(addressRepository, Mockito.times(1)).delete(address);
    }

    @Test
    void deleteAddressFailNotFound() {
        long addressId = 1L;

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        try {
            addressService.deleteAddress(addressId);
        } catch (Exception e) {
            assert e instanceof AddressNotFoundException;
        }

        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
    }
}
