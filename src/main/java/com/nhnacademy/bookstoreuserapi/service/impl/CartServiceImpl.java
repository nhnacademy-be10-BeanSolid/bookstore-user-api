package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Cart;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;



    @Override
    public ResponseCart addCart(CartCreateRequest cart){
        ResponseCart findCart = cartRepository.findByUserIdAndBookId(cart.userId(), cart.bookId());
        if (findCart != null) {
            throw new CartAlreadyExistException(cart.userId(), cart.bookId());
        }
        User user = userRepository.findById(cart.userId())
                .orElseThrow(() -> new UserNotFoundException(cart.userId()));
        Cart savedCart = cartRepository.save(new Cart(cart, user));
        return new ResponseCart(
                savedCart.getCartId(),
                savedCart.getBookId(),
                savedCart.getUser().getUserId(),
                savedCart.getQuantity()
        );
    }

    @Override
    public Optional<ResponseCart> editCart(long cartId, CartUpdateRequest cart) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        if (cart.quantity() == 0) {
            deleteCart(cartId);
            return Optional.empty();
        }
        findCart.setQuantity(cart.quantity());
        return Optional.of(new ResponseCart(
                findCart.getCartId(),
                findCart.getBookId(),
                findCart.getUser().getUserId(),
                findCart.getQuantity()
        ));
    }

    @Override
    public ResponseCart getCart(long cartId) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        return new ResponseCart(
                findCart.getCartId(),
                findCart.getBookId(),
                findCart.getUser().getUserId(),
                findCart.getQuantity()
        );
    }

    @Override
    public Page<ResponseCart> getCartsByUserId(String userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return cartRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public void deleteCart(long cartId) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        cartRepository.delete(findCart);
    }

    @Override
    public void deleteCartsByUserId(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Cart> carts = cartRepository.findAllByUser_UserId(userId);
        if (carts.isEmpty()) {
            return;
        }
        cartRepository.deleteAll(carts);
    }
}
