package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Cart;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestCart;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestCart;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.InvalidDataException;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public ResponseCart addCart(SignUpRequestCart cart){
        if(cart == null || cart.getUserId() == null || cart.getBookId() <= 0L) {
            throw new InvalidDataException("Invalid cart data");
        }
        Cart findCart = cartRepository.findByUser_UserIdAndBookId(cart.getUserId(), cart.getBookId());
        if (findCart != null) {
            throw new CartAlreadyExistException(cart.getUserId(), cart.getBookId());
        }
        User user = userRepository.findById(cart.getUserId())
                .orElseThrow(() -> new UserNotFoundException(cart.getUserId()));
        Cart savedCart = cartRepository.save(new Cart(cart, user));
        return new ResponseCart(
                savedCart.getCartId(),
                savedCart.getBookId(),
                savedCart.getUser().getUserId(),
                savedCart.getQuantity()
        );
    }

    public Optional<ResponseCart> editCart(long cartId, EditRequestCart cart) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        if (cart.getQuantity() <= 0) {
            deleteCart(cartId);
            return Optional.empty();
        }
        findCart.setQuantity(cart.getQuantity());
        return Optional.of(new ResponseCart(
                findCart.getCartId(),
                findCart.getBookId(),
                findCart.getUser().getUserId(),
                findCart.getQuantity()
        ));
    }

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

    public List<ResponseCart> getCartsByUserId(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Cart> carts = cartRepository.findAllByUser_UserId(userId);
        return carts.stream()
                .map(cart -> new ResponseCart(
                        cart.getCartId(),
                        cart.getBookId(),
                        cart.getUser().getUserId(),
                        cart.getQuantity()))
                .toList();
    }

    public void deleteCart(long cartId) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        cartRepository.delete(findCart);
    }

    public void deleteCartsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Cart> carts = cartRepository.findAllByUser_UserId(userId);
        if (carts.isEmpty()) {
            return;
        }
        cartRepository.deleteAll(carts);
    }
}
