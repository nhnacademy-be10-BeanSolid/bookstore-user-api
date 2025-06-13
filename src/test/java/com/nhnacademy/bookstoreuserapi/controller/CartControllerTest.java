package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import com.nhnacademy.bookstoreuserapi.exception.CartAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.CartNotFoundException;
import com.nhnacademy.bookstoreuserapi.service.CartService;
import com.nhnacademy.bookstoreuserapi.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

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
        Mockito.when(cartService.addCart(cart)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).addCart(cart);
    }

    @Test
    void addCartFailAlreadyExist() throws Exception {
        CartCreateRequest cart = new CartCreateRequest(1, "user123", 3);
        Mockito.when(cartService.addCart(cart)).thenThrow(new CartAlreadyExistException("user123", 1L));
        mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isConflict());
        Mockito.verify(cartService, Mockito.times(1)).addCart(cart);
    }

    @Test
    void editCart() throws Exception {
        long cartId = 1L;
        CartUpdateRequest cart = new CartUpdateRequest(5);
        Mockito.when(cartService.editCart(cartId, cart)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/carts/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).editCart(cartId, cart);
    }

    @Test
    void getCart() throws Exception {
        long cartId = 1L;
        Mockito.when(cartService.getCart(cartId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/" + cartId))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).getCart(cartId);
    }

    @Test
    void getCartFailNotFound() throws Exception {
        long cartId = 1L;
        Mockito.when(cartService.getCart(cartId)).thenThrow(new CartNotFoundException(cartId));
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/" + cartId))
                .andExpect(status().isNotFound());
        Mockito.verify(cartService, Mockito.times(1)).getCart(cartId);
    }

    @Test
    void getCartsByUserId() throws Exception {
        String userId = "user123";
        Mockito.when(cartService.getCartsByUserId(userId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/carts/user/" + userId))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).getCartsByUserId(userId);
    }

    @Test
    void deleteCart() throws Exception {
        long cartId = 1L;
        Mockito.doNothing().when(cartService).deleteCart(cartId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/" + cartId))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).deleteCart(cartId);
    }

    @Test
    void deleteCartsByUserId() throws Exception {
        String userId = "user123";
        Mockito.doNothing().when(cartService).deleteCartsByUserId(userId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/user/" + userId))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(cartService, Mockito.times(1)).deleteCartsByUserId(userId);
    }
}
