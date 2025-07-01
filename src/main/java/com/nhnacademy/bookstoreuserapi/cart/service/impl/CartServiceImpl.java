package com.nhnacademy.bookstoreuserapi.cart.service.impl;

import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.service.CartService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.ResponseCart;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartRepository;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.common.exception.OwnerShipValidator.validate;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;



    @Override
    public ResponseCart addCart(String userId, CartCreateRequest cart){
        validate(userId, cart.userId());
        ResponseCart findCart = cartRepository.findByUserIdAndBookId(cart.userId(), cart.bookId());
        if (findCart != null) {
            throw new CartAlreadyExistException(cart.userId(), cart.bookId());
        }
        User user = userRepository.findByUserId(cart.userId());

        if (user == null) {
            throw new UserNotFoundException(cart.userId());
        }
        Cart savedCart = cartRepository.save(new Cart(cart, user));
        return new ResponseCart(
                savedCart.getCartId(),
                savedCart.getBookId(),
                savedCart.getUser().getUserId(),
                savedCart.getQuantity()
        );
    }

    @Override
    public Optional<ResponseCart> editCart(String userId, long cartId, CartUpdateRequest cart) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        validate(userId, findCart.getUser().getUserId());
        if (cart.quantity() == 0) {
            deleteCart(userId, cartId);
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
    public ResponseCart getCart(String userId, long cartId) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        validate(userId, findCart.getUser().getUserId());
        return new ResponseCart(
                findCart.getCartId(),
                findCart.getBookId(),
                findCart.getUser().getUserId(),
                findCart.getQuantity()
        );
    }

    @Override
    public Page<ResponseCart> getCartsByUserId(String userId, Pageable pageable) {
        if (!userRepository.existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        return cartRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public void deleteCart(String userId, long cartId) {
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        validate(userId, findCart.getUser().getUserId());
        cartRepository.delete(findCart);
    }

    @Override
    public void deleteCartsByUserId(String userId) {

        if (!userRepository.existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        List<Cart> carts = cartRepository.findAllByUser_UserId(userId);
        if (carts.isEmpty()) {
            return;
        }
        cartRepository.deleteAll(carts);
    }
}
