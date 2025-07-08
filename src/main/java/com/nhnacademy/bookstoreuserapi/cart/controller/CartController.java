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
    public ResponseEntity<Void> addItemToCart(
            CartContext ctx,
            @Valid @RequestBody CartAddItemRequest request,
            BindingResult bindingResult
    ) {
        validate(bindingResult);
        cartService.addItem(ctx, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Void> updateItemQuantity(
            CartContext ctx,
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody CartUpdateRequest request,
            BindingResult bindingResult
    ) {
        validate(bindingResult);
        cartService.updateItem(ctx, itemId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItemFromCart(
            CartContext ctx,
            @PathVariable("itemId") Long itemId
    ) {
        cartService.deleteItem(ctx, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteItemsFromCart(
            CartContext ctx,
            @RequestBody List<Long> itemIds
    ) {
        cartService.deleteItems(ctx, itemIds);
        return ResponseEntity.noContent().build();
    }

    private void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
    }
}
