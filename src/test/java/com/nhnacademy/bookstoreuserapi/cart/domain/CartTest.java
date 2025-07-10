package com.nhnacademy.bookstoreuserapi.cart.domain;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("testUser");
        cart = new Cart(user);
    }

    @Test
    void addItem_new_success() {
        cart.addItem(1L, 1);
        assertEquals(1, cart.getItems().size());
        assertEquals(1L, cart.getItems().getFirst().getItemId());
        assertEquals(1, cart.getItems().getFirst().getQuantity());
    }

    @Test
    void addItem_duplicate_throwsException() {
        cart.addItem(1L, 1);
        assertThrows(DataIntegrityViolationException.class, () -> cart.addItem(1L, 2));
    }

    @Test
    void removeItem_success() {
        cart.addItem(1L, 1);
        CartItem itemToRemove = cart.getItems().stream()
                .filter(i -> i.getItemId().equals(1L))
                .findFirst()
                .orElse(null);
        assertNotNull(itemToRemove);

        cart.removeItem(1L);

        assertEquals(0, cart.getItems().size());
        assertNull(itemToRemove.getCart());
    }

    @Test
    void removeItem_notFound() {
        cart.addItem(1L, 1);
        cart.removeItem(99L);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void constructor_user() {
        Cart userCart = new Cart(user);
        assertEquals(user, userCart.getUser());
        assertEquals(OwnerType.USER, userCart.getOwnerType());
    }

    @Test
    void constructor_guest() {
        String guestUUID = "test-guest-uuid";
        Cart guestCart = new Cart(guestUUID);
        assertEquals(guestUUID, guestCart.getGuestUUID());
        assertEquals(OwnerType.GUEST, guestCart.getOwnerType());
    }
}
