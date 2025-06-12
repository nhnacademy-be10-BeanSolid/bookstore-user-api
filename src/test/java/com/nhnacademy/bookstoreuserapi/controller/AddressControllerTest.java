package com.nhnacademy.bookstoreuserapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestAddress;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.exception.AddressAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.AddressNotFoundException;
import com.nhnacademy.bookstoreuserapi.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressServiceImpl addressService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addAddress() throws Exception{
        SignUpRequestAddress address = new SignUpRequestAddress("별칭", "광주광역시 도로명주소 123", "userId123");
        Mockito.when(addressService.save(address)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(addressService, Mockito.times(1)).save(address);
    }

    @Test
    void addAddressFail() throws Exception{
        SignUpRequestAddress address = new SignUpRequestAddress("별칭", "광주광역시 도로명주소 123", "userId123");
        Mockito.when(addressService.save(address)).thenThrow(AddressAlreadyExistException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteAddress() throws Exception{

        Mockito.doNothing().when(addressService).deleteAddress( 1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/address/{addressId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAddressFail() throws Exception{

        Mockito.doThrow(AddressNotFoundException.class).when(addressService).deleteAddress( 1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/address/{addressId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAddress() throws Exception{
        Mockito.when(addressService.getAddress(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/address/{addressId}", 1L))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(addressService, Mockito.times(1)).getAddress(1L);
    }

    @Test
    void getAddresses() throws Exception{
        ResponseAddress responseAddress = new ResponseAddress(1L, "별칭", "광주광역시 도로명주소 123", "userId123");
        ResponseAddress responseAddress2 = new ResponseAddress(2L, "별칭2", "서울특별시 도로명주소 456", "userId123");
        Mockito.when(addressService.getAllAddresses("userId123"))
                .thenReturn(List.of(responseAddress, responseAddress2));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/address/user/{userId}", "userId123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("별칭"));
        Assertions.assertTrue(content.contains("별칭2"));
        Mockito.verify(addressService, Mockito.times(1)).getAllAddresses("userId123");
    }
}
