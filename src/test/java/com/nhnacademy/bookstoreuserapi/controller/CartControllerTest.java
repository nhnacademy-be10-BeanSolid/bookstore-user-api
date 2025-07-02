package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.cart.controller.CartController;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.domain.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.cart.service.CartService;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addCart() throws Exception {
        CartCreateRequest cart = new CartCreateRequest(1, "user123", 3);
        Mockito.when(cartService.addCart("user123", cart)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/carts/me")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).addCart("user123", cart);
    }

    @Test
    void addCartFailAlreadyExist() throws Exception {
        CartCreateRequest cart = new CartCreateRequest(1, "user123", 3);
        Mockito.when(cartService.addCart("user123", cart)).thenThrow(new CartAlreadyExistException("user123", 1L));
        mockMvc.perform(MockMvcRequestBuilders.post("/carts/me")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isConflict());
        Mockito.verify(cartService, Mockito.times(1)).addCart("user123", cart);
    }

    @Test
    void addCartFailValidation() throws Exception {
        CartCreateRequest cart = new CartCreateRequest(0, "", 0);

        mockMvc.perform(MockMvcRequestBuilders.post("/carts/me")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(result ->
                        assertInstanceOf(ValidationFailedException.class, result.getResolvedException()));
    }

    @Test
    void editCart() throws Exception {
        long cartId = 1L;
        CartUpdateRequest cart = new CartUpdateRequest(5);
        Mockito.when(cartService.editCart("user123", cartId, cart)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/carts/me/" + cartId)
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).editCart("user123", cartId, cart);
    }

    @Test
    void editCartFailValidation() throws Exception {
        long cartId = 1L;
        CartUpdateRequest cart = new CartUpdateRequest(-1);

        mockMvc.perform(MockMvcRequestBuilders.put("/carts/me/" + cartId)
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(result ->
                        assertInstanceOf(ValidationFailedException.class, result.getResolvedException()));
    }

    @Test
    void getCart() throws Exception {
        long cartId = 1L;
        Mockito.when(cartService.getCart("user123", cartId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/me/" + cartId)
                        .header("X-USER-ID", "user123"))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).getCart("user123", cartId);
    }

    @Test
    void getCartFailNotFound() throws Exception {
        long cartId = 1L;
        Mockito.when(cartService.getCart("user123", cartId)).thenThrow(new CartNotFoundException(cartId));
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/me/" + cartId)
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isNotFound());
        Mockito.verify(cartService, Mockito.times(1)).getCart("user123", cartId);
    }

    @Test
    void getCartsByUserId() throws Exception {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0,20);
        Mockito.when(cartService.getCartsByUserId(userId, pageable)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/me")
                        .header("X-USER-ID", userId))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).getCartsByUserId(userId, pageable);
    }

    @Test
    void getCartsByUserIdFailBlank() throws Exception {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0,20);
        Mockito.when(cartService.getCartsByUserId(userId, pageable)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/me")
                        .header("X-USER-ID", ""))
                .andExpect(status().is4xxClientError());
        Mockito.verify(cartService, Mockito.times(0)).getCartsByUserId(userId, pageable);
    }

    @Test
    void getCartsByUserIdFailExceed255Letter() throws Exception {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0,20);
        Mockito.when(cartService.getCartsByUserId(userId, pageable)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/me")
                        .header("X-USER-ID", "asfghjklqwertyuiopzxcvbnm".repeat(20)))
                .andExpect(status().is4xxClientError());
        Mockito.verify(cartService, Mockito.times(0)).getCartsByUserId(userId, pageable);
    }

    @Test
    void deleteCart() throws Exception {
        long cartId = 1L;
        Mockito.doNothing().when(cartService).deleteCart("user123", cartId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/me/" + cartId)
                        .header("X-USER-ID", "user123"))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).deleteCart("user123", cartId);
    }

    @Test
    void deleteCartsByUserId() throws Exception {
        String userId = "user123";
        Mockito.doNothing().when(cartService).deleteCartsByUserId(userId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/me")
                        .header("X-USER-ID", userId))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).deleteCartsByUserId(userId);
    }

    @Test
    void deleteCartsByUserIdFailBlank() throws Exception {
        String userId = "user123";
        Mockito.doNothing().when(cartService).deleteCartsByUserId(userId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/me")
                        .header("X-USER-ID", ""))
                .andExpect(status().is4xxClientError());
        Mockito.verify(cartService, Mockito.times(0)).deleteCartsByUserId(userId);
    }

    @Test
    void deleteCartsByUserIdFailExceed255Letter() throws Exception {
        String userId = "user123";
        Mockito.doNothing().when(cartService).deleteCartsByUserId(userId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/me")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm".repeat(20)))
                .andExpect(status().is4xxClientError());
        Mockito.verify(cartService, Mockito.times(0)).deleteCartsByUserId(userId);
    }
}
