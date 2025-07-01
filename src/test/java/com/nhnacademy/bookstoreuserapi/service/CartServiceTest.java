package com.nhnacademy.bookstoreuserapi.service;


import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.ResponseCart;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.cart.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        Mockito.when(cartRepository.findByUserIdAndBookId("user123", 1L)).thenReturn(null);
        Mockito.when(userRepository.findByUserId("user123")).thenReturn(user);
        Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
        cartService.addCart("user123", cartCreateRequest);
        Mockito.verify(cartRepository, Mockito.times(1)).findByUserIdAndBookId("user123", 1L);
        Mockito.verify(userRepository, Mockito.times(1)).findByUserId("user123");
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
        Mockito.when(cartRepository.findByUserIdAndBookId("user123", 1L)).thenReturn(new ResponseCart(
                cart.getCartId(), cart.getBookId(), cart.getUser().getUserId(), cart.getQuantity()));
        Assertions.assertThrows(CartAlreadyExistException.class, () -> cartService.addCart("user123", cartCreateRequest));
        Mockito.verify(cartRepository, Mockito.times(1)).findByUserIdAndBookId("user123", 1L);
        Mockito.verify(userRepository, Mockito.never()).findByUserId(Mockito.anyString());
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
        Optional<ResponseCart> updatedCart = cartService.editCart("user123", cartId, cartUpdateRequest);
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
        Optional<ResponseCart> updatedCart = cartService.editCart("user123", cartId, cartUpdateRequest);
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
        cartService.getCart("user123", cartId);
        Mockito.verify(cartRepository, Mockito.times(1)).findById(cartId);
    }

    @Test
    void getCartFailNotFound() {
        long cartId = 1L;
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
        Assertions.assertThrows(CartNotFoundException.class, () -> cartService.getCart("user123", cartId));
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
        ResponseCart expectedResponse = new ResponseCart(
                existingCart.getCartId(),
                existingCart.getBookId(),
                existingCart.getUser().getUserId(),
                existingCart.getQuantity());
        Pageable pageable = PageRequest.of(0,20);

        Page<ResponseCart> page = new PageImpl<>(List.of(expectedResponse), pageable, 1);

        Mockito.when(userRepository.existsByUserId("user123")).thenReturn(true);
        Mockito.when(cartRepository.findAllByUserId("user123", pageable)).thenReturn(page);
        Page<ResponseCart> result = cartService.getCartsByUserId("user123", pageable);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(expectedResponse, result.getContent().get(0));
        Mockito.verify(userRepository, Mockito.times(1)).existsByUserId("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).findAllByUserId("user123", pageable);
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
        Mockito.when(userRepository.existsByUserId("user123")).thenReturn(true);
        Mockito.when(cartRepository.findAllByUser_UserId("user123")).thenReturn(List.of(existingCart));
        Mockito.doNothing().when(cartRepository).deleteAll(List.of(existingCart));
        cartService.deleteCartsByUserId("user123");
        Mockito.verify(userRepository, Mockito.times(1)).existsByUserId("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).findAllByUser_UserId("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).deleteAll(List.of(existingCart));
    }

}
