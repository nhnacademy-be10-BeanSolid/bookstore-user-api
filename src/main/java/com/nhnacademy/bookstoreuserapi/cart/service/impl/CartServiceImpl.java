package com.nhnacademy.bookstoreuserapi.cart.service.impl;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartItem;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartItemDto;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartItemRepository;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.cart.service.CartService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public CartCreateResponse createCart(CartContext context) {
        if (context.isUser()) {
            return createUserCart(context.getUserId());
        } else {
            return createGuestCart();
        }
    }

    private CartCreateResponse createUserCart(String userId) {
        if (cartRepository.existsByUserIdAndOwnerType(userId, OwnerType.USER)) {
            throw new CartAlreadyExistsException(userId);
        }
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = new Cart(user);
        Cart saved = cartRepository.save(cart);

        return CartCreateResponse.builder()
                .cartId(saved.getCartId())
                .userId(user.getUserId())
                .ownerType(saved.getOwnerType())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private CartCreateResponse createGuestCart() {
        String guestUUID = UUID.randomUUID().toString();
        Cart cart = new Cart(guestUUID);
        Cart saved = cartRepository.save(cart);
        return CartCreateResponse.builder()
                .cartId(saved.getCartId())
                .guestUUID(saved.getGuest_uuid())
                .ownerType(saved.getOwnerType())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public CartResponse getCart(CartContext context) {
        Cart cart = findCart(context);
        List<CartItemDto> items = cartItemRepository.findAllByCart(cart).stream()
                .map(item -> new CartItemDto(item.getBookId(), item.getQuantity()))
                .collect(Collectors.toList());
        return new CartResponse(cart.getCartId(), items);
    }

    @Override
    public void addItem(CartContext context, CartAddItemRequest request) {
        Cart cart = findCart(context);
        CartItem item = new CartItem(cart, request.getItemId(), request.getQuantity());
        cartItemRepository.save(item);
    }

    @Override
    public void updateItem(CartContext context, Long itemId, CartUpdateRequest request) {
        Cart cart = findCart(context);
        CartItem item = cartItemRepository.findByCartAndBookId(cart, itemId)
                .orElseThrow(() -> new CartNotFoundException(itemId));
        item.updateQuantity(request.quantity());
        cartItemRepository.save(item);
    }

    @Override
    public void deleteItem(CartContext context, Long itemId) {
        Cart cart = findCart(context);
        cartItemRepository.deleteByCartAndBookId(cart, itemId);
    }

    @Override
    public void deleteItems(CartContext context, List<Long> itemIds) {
        Cart cart = findCart(context);
        cartItemRepository.deleteAllByCartAndBookIdIn(cart, itemIds);
    }

    private Cart findCart(CartContext context) {
        if (context.isUser()) {
            return cartRepository.findByUserId(context.getUserId())
                    .orElseThrow(() -> new CartNotFoundException(0));
        } else {
            return cartRepository.findByGuestUUID(context.getGuestUUID())
                    .orElseThrow(() -> new CartNotFoundException(0));
        }
    }
}
