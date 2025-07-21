package com.nhnacademy.bookstoreuserapi.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateItemsRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartItemDto;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.cart.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("[POST] /carts/me - Create Cart")
    void createCart() throws Exception {
        CartCreateResponse mockResponse = CartCreateResponse.builder()
                .cartId(1L)
                .userId("user-1")
                .guestUUID(null)
                .ownerType(OwnerType.USER)
                .createdAt(LocalDateTime.now())
                .build();

        when(cartService.createCart(any(CartContext.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/carts/me")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.ownerType").value("USER"));
    }

    @Test
    @DisplayName("[GET] /carts/me - Get Cart")
    void getCart() throws Exception {
        CartResponse mockResponse = CartResponse.builder()
                .cartId(1L)
                .items(List.of(
                        CartItemDto.builder().itemId(10L).quantity(2).build(),
                        CartItemDto.builder().itemId(20L).quantity(5).build()
                ))
                .build();

        when(cartService.getCart(any(CartContext.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/carts/me")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.items[0].itemId").value(10L))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    @DisplayName("[POST] /carts/me/items - Add Item")
    void addItemToCart() throws Exception {
        CartAddItemRequest req = new CartAddItemRequest(10L, 2);
        CartResponse mockResponse = CartResponse.builder()
                .cartId(1L)
                .items(List.of(CartItemDto.builder().itemId(10L).quantity(2).build()))
                .build();

        when(cartService.addItem(any(CartContext.class), any())).thenReturn(mockResponse);

        mockMvc.perform(post("/carts/me/items")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.items[0].itemId").value(10L));
    }

    @Test
    @DisplayName("[PUT] /carts/me/items/{itemId} - Update Item Quantity")
    void updateItemQuantity() throws Exception {
        CartUpdateRequest req = new CartUpdateRequest(5);
        CartResponse mockResponse = CartResponse.builder()
                .cartId(1L)
                .items(List.of(CartItemDto.builder().itemId(7L).quantity(5).build()))
                .build();

        when(cartService.updateItem(any(CartContext.class), eq(7L), any(CartUpdateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/carts/me/items/7")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.items[0].itemId").value(7L))
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    @DisplayName("[DELETE] /carts/me/items/{itemId} - Delete Item")
    void deleteItemFromCart() throws Exception {
        CartResponse mockResponse = CartResponse.builder()
                .cartId(1L)
                .items(List.of())
                .build();

        when(cartService.deleteItem(any(CartContext.class), eq(7L))).thenReturn(mockResponse);

        mockMvc.perform(delete("/carts/me/items/7")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L));
    }

    @Test
    @DisplayName("[DELETE] /carts/me/items - Delete Items")
    void deleteItemsFromCart() throws Exception {
        List<Long> itemIds = List.of(2L, 3L, 4L);
        CartResponse mockResponse = CartResponse.builder()
                .cartId(1L)
                .items(List.of())
                .build();

        when(cartService.deleteItems(any(CartContext.class), any())).thenReturn(mockResponse);

        mockMvc.perform(delete("/carts/me/items")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L));
    }

    @Test
    @DisplayName("[PUT] /carts/me/items - Update Multiple Items")
    void updateItemsInCart() throws Exception {
        var updateReq = new CartUpdateItemsRequest(List.of(
                new CartUpdateItemsRequest.CartItemUpdate(10L, 2),
                new CartUpdateItemsRequest.CartItemUpdate(20L, 3)
        ));
        CartResponse mockResponse = CartResponse.builder()
                .cartId(1L)
                .items(List.of(
                        CartItemDto.builder().itemId(10L).quantity(2).build(),
                        CartItemDto.builder().itemId(20L).quantity(3).build()
                ))
                .build();

        when(cartService.updateItems(any(CartContext.class), any())).thenReturn(mockResponse);

        mockMvc.perform(put("/carts/me/items")
                        .header("X-OWNER-TYPE", "USER")
                        .header("X-USER-ID", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.items[?(@.itemId==10)].quantity").value(2))
                .andExpect(jsonPath("$.items[?(@.itemId==20)].quantity").value(3));
    }
}
