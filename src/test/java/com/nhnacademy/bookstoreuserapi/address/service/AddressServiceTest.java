package com.nhnacademy.bookstoreuserapi.address.service;

import com.nhnacademy.bookstoreuserapi.address.domain.Address;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.address.domain.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.address.exception.AddressAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.address.exception.AddressLimitExceededException;
import com.nhnacademy.bookstoreuserapi.address.exception.AddressNotFoundException;
import com.nhnacademy.bookstoreuserapi.address.repository.AddressRepository;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.address.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AddressServiceImpl addressService;

    @Test
    void saveAddress() {
        AddressCreateRequest addressCreateRequest = new AddressCreateRequest("Home", "123 Main St", "user123");
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("test")
                .userPassword("plainPassword")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(userGrade)
                .build();
        Address address = new Address(0L, "Home", "123 Main St", user);

        Mockito.when(addressRepository.countByUser_UserId(addressCreateRequest.userId())).thenReturn(1L);
        Mockito.when(addressRepository.existsByUser_UserIdAndAddressDetail(addressCreateRequest.userId(), addressCreateRequest.addressDetail())).thenReturn(false);
        Mockito.when(addressRepository.save(address)).thenReturn(address);
        Mockito.when(userRepository.findByUserId(addressCreateRequest.userId())).thenReturn(user);

        addressService.save("user123", addressCreateRequest);
        assertAccessDenied(() -> addressService.save("anotherUser", addressCreateRequest));
        Mockito.verify(addressRepository, Mockito.times(1)).countByUser_UserId(addressCreateRequest.userId());
        Mockito.verify(addressRepository, Mockito.times(1)).existsByUser_UserIdAndAddressDetail(addressCreateRequest.userId(), addressCreateRequest.addressDetail());
        Mockito.verify(addressRepository, Mockito.times(1)).save(address);
        Mockito.verify(userRepository, Mockito.times(1)).findByUserId(addressCreateRequest.userId());
    }

    @Test
    void saveAddressFailAlreadyExists() {
        AddressCreateRequest addressCreateRequest = new AddressCreateRequest("Home", "123 Main St", "user123");

        Mockito.when(addressRepository.existsByUser_UserIdAndAddressDetail(addressCreateRequest.userId(), addressCreateRequest.addressDetail())).thenReturn(true);

        try {
            addressService.save("user123", addressCreateRequest);
        } catch (Exception e) {
            assert e instanceof AddressAlreadyExistException;
        }

        Mockito.verify(addressRepository, Mockito.times(1)).existsByUser_UserIdAndAddressDetail(addressCreateRequest.userId(), addressCreateRequest.addressDetail());
    }

    @Test
    void saveAddressFailLimitExceeded() {
        AddressCreateRequest addressCreateRequest = new AddressCreateRequest("Home", "123 Main St", "user123");
        Mockito.when(addressRepository.countByUser_UserId(addressCreateRequest.userId())).thenReturn(10L);
        try {
            addressService.save("user123", addressCreateRequest);
        } catch (Exception e) {
            assert e instanceof AddressLimitExceededException;
        }
        Mockito.verify(addressRepository, Mockito.times(1)).countByUser_UserId(addressCreateRequest.userId());
    }

    @Test
    void getAddress() {
        long addressId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("test")
                .userPassword("plainPassword")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(userGrade)
                .build();
        Address address = new Address(addressId, "Home", "123 Main St", user);

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.getAddress("test", addressId);
        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
    }

    @Test
    void getAddressFailNotFound() {
        long addressId = 1L;

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        try {
            addressService.getAddress("user123",addressId);
        } catch (Exception e) {
            assert e instanceof AddressNotFoundException;
        }

        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
    }

    @Test
    void getAllAddresses() {
        String userId = "user123";
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId(userId)
                .userPassword("plainPassword")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(userGrade)
                .build();
        Address address = new Address(1L, "Home", "123 Main St", user);

        Mockito.when(addressRepository.findAllByUserId(userId)).thenReturn(List.of(
                new ResponseAddress(address.getAddressId(), address.getAddressNickName(), address.getAddressDetail(), userId)
        ));

        addressService.getAllAddresses(userId);
        Mockito.verify(addressRepository, Mockito.times(1)).findAllByUserId(userId);
    }

    @Test
    void deleteAddress() {
        long addressId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("test")
                .userPassword("plainPassword")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(userGrade)
                .build();
        Address address = new Address(addressId, "Home", "123 Main St", user);

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        Mockito.doNothing().when(addressRepository).delete(address);

        addressService.deleteAddress("test", addressId);
        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
        Mockito.verify(addressRepository, Mockito.times(1)).delete(address);
    }

    @Test
    void deleteAddressFailNotFound() {
        long addressId = 1L;

        Mockito.when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        try {
            addressService.deleteAddress("user123", addressId);
        } catch (Exception e) {
            assert e instanceof AddressNotFoundException;
        }

        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
    }
    void assertAccessDenied(Runnable action) {
        assertThrows(AccessDeniedException.class, action::run);
    }
}
