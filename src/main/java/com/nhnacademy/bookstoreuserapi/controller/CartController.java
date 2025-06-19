package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts/me")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ResponseCart> addCart(@AuthenticatedUserId  String userId, @Valid @RequestBody CartCreateRequest cart, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCart(userId, cart));
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<ResponseCart> updateCart(@AuthenticatedUserId String userId, @PathVariable @NotNull @Min(1) Long cartId, @Valid @RequestBody CartUpdateRequest cart, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        Optional<ResponseCart> result = cartService.editCart(userId, cartId, cart);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ResponseCart> getCart(@AuthenticatedUserId String userId, @PathVariable @NotNull @Min(1) Long cartId) {
        return ResponseEntity.ok().body(cartService.getCart(userId, cartId));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseCart>> getCartByUserId(@AuthenticatedUserId String userId, Pageable pageable) {
        return ResponseEntity.ok().body(cartService.getCartsByUserId(userId, pageable));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@AuthenticatedUserId String userId, @PathVariable @NotNull @Min(1) Long cartId) {
        cartService.deleteCart(userId, cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCartsByUserId(@AuthenticatedUserId String userId) {
        cartService.deleteCartsByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
