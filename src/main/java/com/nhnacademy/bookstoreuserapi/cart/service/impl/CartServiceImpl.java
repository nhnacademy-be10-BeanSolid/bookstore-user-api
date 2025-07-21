package com.nhnacademy.bookstoreuserapi.cart.service.impl;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartItemDto;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartItemNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.cart.service.CartService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.cart.exception.GuestCartCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private static final int MAX_ATTEMPTS = 5;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public CartCreateResponse createCart(CartContext context) {
        if (context.isUser()) {
            return createUserCart(context.getUserId());
        } else {
            return createGuestCartWithRetry();
        }
    }

    private CartCreateResponse createUserCart(String userId) {
        if (cartRepository.existsByUser_UserId(userId)) {
            throw new CartAlreadyExistsException(userId);
        }
        if(!userRepository.existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        User user = userRepository.findByUserId(userId);

        Cart cart = new Cart(user);
        Cart saved = cartRepository.save(cart);

        return CartCreateResponse.builder()
                .cartId(saved.getCartId())
                .userId(user.getUserId())
                .ownerType(saved.getOwnerType())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private CartCreateResponse createGuestCartWithRetry() {
        for(int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String guestUUID = UUID.randomUUID().toString();
            Cart cart = new Cart(guestUUID);
            try {
                Cart saved = cartRepository.save(cart);
                return CartCreateResponse.builder()
                        .cartId(saved.getCartId())
                        .guestUUID(saved.getGuestUUID())
                        .ownerType(saved.getOwnerType())
                        .createdAt(saved.getCreatedAt())
                        .build();
            } catch (DataIntegrityViolationException e) {
                if(attempt == MAX_ATTEMPTS) {
                    throw new GuestCartCreationException("UUID 생성 재시도 실패");
                }
            }
        }
        throw new GuestCartCreationException("Guest Cart 생성 중 알 수 없는 오류");
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(CartContext context) {
        Cart cart = findCart(context);
        return getCurrentCartResponse(cart);
    }

    @Override
    public CartResponse addItem(CartContext context, CartAddItemRequest request) {
        Cart cart = findCart(context);
        cart.addItem(request.itemId(), request.quantity());
        return getCurrentCartResponse(cart);
    }

    @Override
    public CartResponse updateItem(CartContext context, Long itemId, CartUpdateRequest request) {
        Cart cart = findCart(context);
        cart.getItems().stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(itemId))
                .updateQuantity(request.quantity());
        return getCurrentCartResponse(cart);
    }

    @Override
    public CartResponse deleteItem(CartContext context, Long itemId) {
        Cart cart = findCart(context);
        cart.removeItem(itemId);
        return getCurrentCartResponse(cart);
    }

    @Override
    public CartResponse deleteItems(CartContext context, List<Long> itemIds) {
        Cart cart = findCart(context);
        itemIds.forEach(cart::removeItem);
        return getCurrentCartResponse(cart);
    }

    @Override
    public CartResponse updateItems(CartContext context, com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateItemsRequest request) {
        Cart cart = findCart(context);
        request.items().forEach(item ->
            cart.getItems().stream()
                    .filter(i -> i.getItemId().equals(item.bookId()))
                    .findFirst()
                    .orElseThrow(() -> new CartItemNotFoundException(item.bookId()))
                    .updateQuantity(item.quantity())
        );
        return getCurrentCartResponse(cart);
    }



    private Cart findCart(CartContext context) {
        if (context.isUser()) {
            return cartRepository.findByOwnerTypeAndUser_UserId(OwnerType.USER, context.getUserId())
                    .orElseThrow(() -> new CartNotFoundException("해당 사용자(userId: " + context.getUserId() + ")의 장바구니를 찾을 수 없습니다."));
        } else {
            return cartRepository.findByOwnerTypeAndGuestUUID(OwnerType.GUEST, context.getGuestUUID())
                    .orElseThrow(() -> new CartNotFoundException("해당 게스트(UUID: " + context.getGuestUUID() + ")의 장바구니를 찾을 수 없습니다."));
        }
    }

    private CartResponse getCurrentCartResponse(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(CartItemDto::from)
                .toList();

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .items(itemDtos)
                .build();
    }
}
