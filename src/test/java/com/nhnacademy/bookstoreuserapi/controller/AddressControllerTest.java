package com.nhnacademy.bookstoreuserapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.exception.AddressAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.AddressNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.AddressService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addAddress() throws Exception{
        AddressCreateRequest address = new AddressCreateRequest("별칭", "광주광역시 도로명주소 123", "userId123");
        Mockito.when(addressService.save("userId123", address)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/me/address")
                        .header("X-USER-ID", "userId123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(addressService, Mockito.times(1)).save("userId123", address);
    }

    @Test
    void addAddressFailAlreadyExist() throws Exception{
        AddressCreateRequest address = new AddressCreateRequest("별칭", "광주광역시 도로명주소 123", "userId123");
        Mockito.when(addressService.save("userId123", address)).thenThrow(AddressAlreadyExistException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/me/address")
                        .header("X-USER-ID", "userId123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addAddressFailValidation() throws Exception{
        AddressCreateRequest address = new AddressCreateRequest("", "", "");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/me/address")
                        .header("X-USER-ID", "userId123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ValidationFailedException));
    }

    @Test
    void deleteAddress() throws Exception{

        Mockito.doNothing().when(addressService).deleteAddress( "userId123",1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/me/address/{addressId}", 1L)
                        .header("X-USER-ID", "userId123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAddressFail() throws Exception{

        Mockito.doThrow(AddressNotFoundException.class).when(addressService).deleteAddress("userId123", 1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/me/address/{addressId}", 1L)
                        .header("X-USER-ID", "userId123"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAddress() throws Exception{
        Mockito.when(addressService.getAddress("userId123",1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me/address/{addressId}", 1L)
                        .header("X-USER-ID", "userId123"))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(addressService, Mockito.times(1)).getAddress("userId123",1L);
    }

    @Test
    void getAddresses() throws Exception{
        ResponseAddress responseAddress = new ResponseAddress(1L, "별칭", "광주광역시 도로명주소 123", "userId123");
        ResponseAddress responseAddress2 = new ResponseAddress(2L, "별칭2", "서울특별시 도로명주소 456", "userId123");
        Mockito.when(addressService.getAllAddresses("userId123"))
                .thenReturn(List.of(responseAddress, responseAddress2));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/me/address")
                        .header("X-USER-ID", "userId123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("별칭"));
        Assertions.assertTrue(content.contains("별칭2"));
        Mockito.verify(addressService, Mockito.times(1)).getAllAddresses("userId123");
    }

    @Test
    void getAddressesFailUserIdBlank() throws Exception{
        ResponseAddress responseAddress = new ResponseAddress(1L, "별칭", "광주광역시 도로명주소 123", "userId123");
        ResponseAddress responseAddress2 = new ResponseAddress(2L, "별칭2", "서울특별시 도로명주소 456", "userId123");
        Mockito.when(addressService.getAllAddresses("userId123"))
                .thenReturn(List.of(responseAddress, responseAddress2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me/address")
                        .header("X-USER-ID", ""))
                .andExpect(status().is4xxClientError())
                .andReturn();

        Mockito.verify(addressService, Mockito.times(0)).getAllAddresses("userId123");
    }

    @Test
    void getAddressesFailUserIdExceed255Letter() throws Exception{
        ResponseAddress responseAddress = new ResponseAddress(1L, "별칭", "광주광역시 도로명주소 123", "userId123");
        ResponseAddress responseAddress2 = new ResponseAddress(2L, "별칭2", "서울특별시 도로명주소 456", "userId123");
        Mockito.when(addressService.getAllAddresses("userId123"))
                .thenReturn(List.of(responseAddress, responseAddress2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me/address")
                        .header("X-USER-ID", "123456789012345678901234567890243"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        Mockito.verify(addressService, Mockito.times(0)).getAllAddresses("userId123");
    }
}
