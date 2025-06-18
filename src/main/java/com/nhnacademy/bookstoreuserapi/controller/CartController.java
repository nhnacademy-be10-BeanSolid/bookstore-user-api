package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.InvalidHeaderException;
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
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ResponseCart> addCart(@Valid @RequestBody CartCreateRequest cart, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCart(cart));
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<ResponseCart> updateCart(@PathVariable @NotNull @Min(1) Long cartId, @Valid @RequestBody CartUpdateRequest cart, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        Optional<ResponseCart> result = cartService.editCart(cartId, cart);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ResponseCart> getCart(@PathVariable @NotNull @Min(1) Long cartId) {
        return ResponseEntity.ok().body(cartService.getCart(cartId));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<ResponseCart>> getCartByUserId(@RequestHeader("X-USER-ID") String userId, Pageable pageable) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_BLANK);
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_TOO_LONG);
        }
        return ResponseEntity.ok().body(cartService.getCartsByUserId(userId, pageable));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable @NotNull @Min(1) Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteCartsByUserId(@RequestHeader("X-USER-ID") String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_BLANK);
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_TOO_LONG);
        }
        cartService.deleteCartsByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
