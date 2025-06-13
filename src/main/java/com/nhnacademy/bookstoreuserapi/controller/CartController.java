package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseCart addCart(@RequestBody CartCreateRequest cart){
        return cartService.addCart(cart);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<ResponseCart> updateCart(@PathVariable Long cartId, @RequestBody CartUpdateRequest cart){
        Optional<ResponseCart> result = cartService.editCart(cartId, cart);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{cartId}")
    public ResponseCart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }

    @GetMapping("/user/{userId}")
    public List<ResponseCart> getCartByUserId(@PathVariable String userId) {
        return cartService.getCartsByUserId(userId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteCartsByUserId(@PathVariable String userId) {
        cartService.deleteCartsByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
