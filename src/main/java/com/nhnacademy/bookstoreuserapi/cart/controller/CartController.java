package com.nhnacademy.bookstoreuserapi.cart.controller;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.cart.service.CartService;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts/me")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartCreateResponse> createCart(CartContext ctx) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(ctx));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(CartContext ctx) {
        return ResponseEntity.ok(cartService.getCart(ctx));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            CartContext ctx,
            @Valid @RequestBody CartAddItemRequest request,
            BindingResult bindingResult
    ) {
        validate(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(ctx, request));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            CartContext ctx,
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody CartUpdateRequest request,
            BindingResult bindingResult
    ) {
        validate(bindingResult);
        return ResponseEntity.ok(cartService.updateItem(ctx, itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> deleteItemFromCart(
            CartContext ctx,
            @PathVariable("itemId") Long itemId
    ) {
        return ResponseEntity.ok(cartService.deleteItem(ctx, itemId));
    }

    @DeleteMapping("/items")
    public ResponseEntity<CartResponse> deleteItemsFromCart(
            CartContext ctx,
            @RequestBody List<Long> itemIds
    ) {
        return ResponseEntity.ok(cartService.deleteItems(ctx, itemIds));
    }

    private void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
    }
}
