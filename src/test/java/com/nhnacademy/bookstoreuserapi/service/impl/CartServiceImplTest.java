package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Cart;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestCart;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestCart;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.InvalidDataException;
import com.nhnacademy.bookstoreuserapi.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CartServiceImpl cartService;

    @Test
    void addCart(){
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(),0, userGrade);
        Cart cart = new Cart(signUpRequestCart, user);
        Mockito.when(cartRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(null);
        Mockito.when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
        cartService.addCart(signUpRequestCart);
        Mockito.verify(cartRepository, Mockito.times(1)).findByUser_UserIdAndBookId("user123", 1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).save(Mockito.any(Cart.class));
    }

    @Test
    void addCartFailInvalidData() {
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(0, null, -1);
        Assertions.assertThrows(InvalidDataException.class, () -> cartService.addCart(signUpRequestCart));
        Mockito.verify(cartRepository, Mockito.never()).findByUser_UserIdAndBookId(Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any(Cart.class));
    }

    @Test
    void addCartFailCartAlreadyExists() {
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(), 0, userGrade);
        Cart cart = new Cart(signUpRequestCart, user);
        Mockito.when(cartRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(cart);
        Assertions.assertThrows(CartAlreadyExistException.class, () -> cartService.addCart(signUpRequestCart));
        Mockito.verify(cartRepository, Mockito.times(1)).findByUser_UserIdAndBookId("user123", 1L);
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any(Cart.class));
    }

    @Test
    void editCart() {
        long cartId = 1L;
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(), 0, userGrade);
        Cart existingCart = new Cart(signUpRequestCart, user);
        existingCart.setCartId(cartId);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        EditRequestCart editRequestCart = new EditRequestCart(5);
        Optional<ResponseCart> updatedCart = cartService.editCart(cartId, editRequestCart);
        Assertions.assertTrue(updatedCart.isPresent());
        Mockito.verify(cartRepository, Mockito.times(1)).findById(cartId);
        Assertions.assertEquals(5, updatedCart.get().getQuantity());
    }

    @Test
    void editCartQuantityZero() {
        long cartId = 1L;
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(), 0, userGrade);
        Cart existingCart = new Cart(signUpRequestCart, user);
        existingCart.setCartId(cartId);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        EditRequestCart editRequestCart = new EditRequestCart(0);
        Optional<ResponseCart> updatedCart = cartService.editCart(cartId, editRequestCart);
        Assertions.assertTrue(updatedCart.isEmpty());
        Mockito.verify(cartRepository, Mockito.times(2)).findById(cartId);
        Mockito.verify(cartRepository,  Mockito.times(1)).delete(existingCart);
    }

    @Test
    void getCart(){
        long cartId = 1L;
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(), 0, userGrade);
        Cart existingCart = new Cart(signUpRequestCart, user);
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
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(), 0, userGrade);
        Cart existingCart = new Cart(signUpRequestCart, user);
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
        SignUpRequestCart signUpRequestCart = new SignUpRequestCart(1, "user123", 3);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = new User(
                "user123", "pass", "김철수", "010-1111-2222", "kim@test.com",
                LocalDate.of(1990, 1, 1), 0, false,
                User.Status.ACTIVE,
                now(), 0, userGrade);
        Cart existingCart = new Cart(signUpRequestCart, user);
        existingCart.setCartId(cartId);
        Mockito.when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findAllByUser_UserId("user123")).thenReturn(List.of(existingCart));
        Mockito.doNothing().when(cartRepository).deleteAll(List.of(existingCart));
        cartService.deleteCartsByUserId("user123");
        Mockito.verify(userRepository, Mockito.times(1)).findById("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).findAllByUser_UserId("user123");
        Mockito.verify(cartRepository, Mockito.times(1)).deleteAll(List.of(existingCart));
    }

    @Test
    void deleteCartsByUserIdFailInvalidDate() {
        String userId = "";
        Assertions.assertThrows(InvalidDataException.class, () -> cartService.deleteCartsByUserId(userId));
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
    }

}
