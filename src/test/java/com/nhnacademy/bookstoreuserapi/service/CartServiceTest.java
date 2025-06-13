package com.nhnacademy.bookstoreuserapi.service;


import com.nhnacademy.bookstoreuserapi.domain.entity.Cart;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Assertions;
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
class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CartServiceImpl cartService;

    @Test
    void addCart(){
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart cart = new Cart(cartCreateRequest, user);
        Mockito.when(cartRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(null);
        Mockito.when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
        cartService.addCart(cartCreateRequest);
        Mockito.verify(cartRepository, Mockito.times(1)).findByUser_UserIdAndBookId("user123", 1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).save(Mockito.any(Cart.class));
    }

    @Test
    void addCartFailCartAlreadyExists() {
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart cart = new Cart(cartCreateRequest, user);
        Mockito.when(cartRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(cart);
        Assertions.assertThrows(CartAlreadyExistException.class, () -> cartService.addCart(cartCreateRequest));
        Mockito.verify(cartRepository, Mockito.times(1)).findByUser_UserIdAndBookId("user123", 1L);
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any(Cart.class));
    }

    @Test
    void editCart() {
        long cartId = 1L;
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart existingCart = new Cart(cartCreateRequest, user);
        existingCart.setCartId(cartId);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        CartUpdateRequest cartUpdateRequest = new CartUpdateRequest(5);
        Optional<ResponseCart> updatedCart = cartService.editCart(cartId, cartUpdateRequest);
        Assertions.assertTrue(updatedCart.isPresent());
        Mockito.verify(cartRepository, Mockito.times(1)).findById(cartId);
        Assertions.assertEquals(5, updatedCart.get().getQuantity());
    }

    @Test
    void editCartQuantityZero() {
        long cartId = 1L;
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart existingCart = new Cart(cartCreateRequest, user);
        existingCart.setCartId(cartId);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        CartUpdateRequest cartUpdateRequest = new CartUpdateRequest(0);
        Optional<ResponseCart> updatedCart = cartService.editCart(cartId, cartUpdateRequest);
        Assertions.assertTrue(updatedCart.isEmpty());
        Mockito.verify(cartRepository, Mockito.times(2)).findById(cartId);
        Mockito.verify(cartRepository,  Mockito.times(1)).delete(existingCart);
    }

    @Test
    void getCart(){
        long cartId = 1L;
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart existingCart = new Cart(cartCreateRequest, user);
        existingCart.setCartId(cartId);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        cartService.getCart(cartId);
        Mockito.verify(cartRepository, Mockito.times(1)).findById(cartId);
    }

    @Test
    void getCartFailNotFound() {
        long cartId = 1L;
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
        Assertions.assertThrows(CartNotFoundException.class, () -> cartService.getCart(cartId));
        Mockito.verify(cartRepository, Mockito.times(1)).findById(cartId);
    }

    @Test
    void getCartsByUserId() {
        long cartId = 1L;
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart existingCart = new Cart(cartCreateRequest, user);
        existingCart.setCartId(cartId);
        Mockito.when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findAllByUser_UserId("user123")).thenReturn(List.of(existingCart));
        cartService.getCartsByUserId("user123");
        Mockito.verify(userRepository, Mockito.times(1)).findById("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).findAllByUser_UserId("user123");
    }

    @Test
    void deleteCartsByUserId() {
        long cartId = 1L;
        CartCreateRequest cartCreateRequest = new CartCreateRequest(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
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
        Cart existingCart = new Cart(cartCreateRequest, user);
        existingCart.setCartId(cartId);
        Mockito.when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findAllByUser_UserId("user123")).thenReturn(List.of(existingCart));
        Mockito.doNothing().when(cartRepository).deleteAll(List.of(existingCart));
        cartService.deleteCartsByUserId("user123");
        Mockito.verify(userRepository, Mockito.times(1)).findById("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).findAllByUser_UserId("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).deleteAll(List.of(existingCart));
    }

}
