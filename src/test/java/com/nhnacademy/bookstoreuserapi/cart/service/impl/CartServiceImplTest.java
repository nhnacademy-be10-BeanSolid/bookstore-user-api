package com.nhnacademy.bookstoreuserapi.cart.service.impl;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateItemsRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartItemNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.exception.GuestCartCreationException;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private CartContext userCartContext;
    private CartContext guestCartContext;
    private User user;
    private Cart userCart;
    private Cart guestCart;

    @BeforeEach
    void setUp() {
        userCartContext = new CartContext(OwnerType.USER, "testUser", null);
        guestCartContext = new CartContext(OwnerType.GUEST, null, "testGuestUUID");

        user = new User();
        user.setUserId("testUser");

        userCart = new Cart(user);
        try {
            java.lang.reflect.Field cartIdField = Cart.class.getDeclaredField("cartId");
            cartIdField.setAccessible(true);
            cartIdField.set(userCart, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        guestCart = new Cart("testGuestUUID");
        try {
            java.lang.reflect.Field cartIdField = Cart.class.getDeclaredField("cartId");
            cartIdField.setAccessible(true);
            cartIdField.set(guestCart, 2L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createCart_user_success() {
        when(userRepository.existsByUserId("testUser")).thenReturn(true);
        when(userRepository.findByUserId("testUser")).thenReturn(user);
        when(cartRepository.existsByUser_UserId("testUser")).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenReturn(userCart);

        CartCreateResponse response = cartService.createCart(userCartContext);

        assertNotNull(response);
        assertEquals(userCart.getCartId(), response.getCartId());
        assertEquals(user.getUserId(), response.getUserId());
        assertEquals(OwnerType.USER, response.getOwnerType());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void createCart_user_alreadyExists() {
        when(cartRepository.existsByUser_UserId("testUser")).thenReturn(true);

        assertThrows(CartAlreadyExistsException.class, () -> cartService.createCart(userCartContext));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void createCart_user_userNotFound() {
        when(cartRepository.existsByUser_UserId("testUser")).thenReturn(false);
        when(userRepository.existsByUserId("testUser")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> cartService.createCart(userCartContext));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void createCart_guest_success() {
        when(cartRepository.save(any(Cart.class))).thenReturn(guestCart);

        CartCreateResponse response = cartService.createCart(guestCartContext);

        assertNotNull(response);
        assertEquals(guestCart.getCartId(), response.getCartId());
        assertEquals(guestCart.getGuestUUID(), response.getGuestUUID());
        assertEquals(OwnerType.GUEST, response.getOwnerType());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void createCart_guest_retrySuccess() {
        when(cartRepository.save(any(Cart.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate UUID"))
                .thenReturn(guestCart);

        CartCreateResponse response = cartService.createCart(guestCartContext);

        assertNotNull(response);
        assertEquals(guestCart.getCartId(), response.getCartId());
        assertEquals(guestCart.getGuestUUID(), response.getGuestUUID());
        verify(cartRepository, times(2)).save(any(Cart.class));
    }

    @Test
    void createCart_guest_retryFailed() {
        when(cartRepository.save(any(Cart.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate UUID"));

        assertThrows(GuestCartCreationException.class, () -> cartService.createCart(guestCartContext));
        verify(cartRepository, times(5)).save(any(Cart.class));
    }

    @Test
    void getCart_success() {
        when(cartRepository.findByOwnerTypeAndUser_UserId(any(OwnerType.class), anyString())).thenReturn(Optional.of(userCart));

        CartResponse response = cartService.getCart(userCartContext);

        assertNotNull(response);
        assertEquals(userCart.getCartId(), response.getCartId());
    }

    @Test
    void getCart_notFound() {
        when(cartRepository.findByOwnerTypeAndUser_UserId(any(OwnerType.class), anyString())).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getCart(userCartContext));
    }

    @Test
    void addItem_new_success() {
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));
        CartAddItemRequest request = new CartAddItemRequest(100L, 1);

        CartResponse response = cartService.addItem(guestCartContext, request);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(100L, response.getItems().getFirst().getItemId());
        assertEquals(1, response.getItems().getFirst().getQuantity());
    }

    @Test
    void addItem_existing_shouldThrowException() {
        guestCart.addItem(100L, 1);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));
        CartAddItemRequest request = new CartAddItemRequest(100L, 2);

        assertThrows(DataIntegrityViolationException.class, () -> cartService.addItem(guestCartContext, request));
    }

    @Test
    void updateItem_success() {
        guestCart.addItem(100L, 1);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));
        CartUpdateRequest request = new CartUpdateRequest(5);

        CartResponse response = cartService.updateItem(guestCartContext, 100L, request);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(100L, response.getItems().getFirst().getItemId());
        assertEquals(5, response.getItems().getFirst().getQuantity());
    }

    @Test
    void updateItem_notFound() {
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));
        CartUpdateRequest request = new CartUpdateRequest(5);

        assertThrows(CartItemNotFoundException.class, () -> cartService.updateItem(guestCartContext, 999L, request));
    }

    @Test
    void deleteItem_success() {
        guestCart.addItem(100L, 1);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));

        CartResponse response = cartService.deleteItem(guestCartContext, 100L);

        assertNotNull(response);
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void deleteItems_success() {
        guestCart.addItem(100L, 1);
        guestCart.addItem(200L, 2);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));

        CartResponse response = cartService.deleteItems(guestCartContext, Arrays.asList(100L, 200L));

        assertNotNull(response);
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void deleteItems_partialSuccess() {
        guestCart.addItem(100L, 1);
        guestCart.addItem(200L, 2);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));

        CartResponse response = cartService.deleteItems(guestCartContext, Collections.singletonList(100L));

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(200L, response.getItems().getFirst().getItemId());
    }

    @Test
    void updateItems_success() {
        guestCart.addItem(100L, 1);
        guestCart.addItem(200L, 2);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));
        CartUpdateItemsRequest request = new CartUpdateItemsRequest(
                Arrays.asList(
                        new CartUpdateItemsRequest.CartItemUpdate(100L, 5),
                        new CartUpdateItemsRequest.CartItemUpdate(200L, 10)
                )
        );

        CartResponse response = cartService.updateItems(guestCartContext, request);

        assertNotNull(response);
        assertEquals(2, response.getItems().size());
        assertEquals(5, response.getItems().stream().filter(i -> i.getItemId() == 100L).findFirst().get().getQuantity());
        assertEquals(10, response.getItems().stream().filter(i -> i.getItemId() == 200L).findFirst().get().getQuantity());
    }

    @Test
    void updateItems_itemNotFound() {
        guestCart.addItem(100L, 1);
        when(cartRepository.findByOwnerTypeAndGuestUUID(any(OwnerType.class), anyString())).thenReturn(Optional.of(guestCart));
        CartUpdateItemsRequest request = new CartUpdateItemsRequest(
                Collections.singletonList(new CartUpdateItemsRequest.CartItemUpdate(999L, 5))
        );

        assertThrows(CartItemNotFoundException.class, () -> cartService.updateItems(guestCartContext, request));
    }
}
