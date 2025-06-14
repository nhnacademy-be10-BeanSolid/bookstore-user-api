package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseCart addCart(@Valid @RequestBody CartCreateRequest cart, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return cartService.addCart(cart);
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
    public ResponseCart getCart(@PathVariable @NotNull @Min(1) Long cartId) {
        return cartService.getCart(cartId);
    }

    @GetMapping("/user/{userId}")
    public List<ResponseCart> getCartByUserId(@PathVariable @NotBlank @Size(max = 20) String userId) {
        return cartService.getCartsByUserId(userId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable @NotNull @Min(1) Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteCartsByUserId(@PathVariable @NotBlank @Size(max = 20) String userId) {
        cartService.deleteCartsByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
