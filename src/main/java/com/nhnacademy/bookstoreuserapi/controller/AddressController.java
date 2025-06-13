package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/address")
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseAddress addAddress(@RequestBody AddressCreateRequest address){
        return addressService.save(address);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{addressId}")
    public ResponseAddress getAddress(@PathVariable long addressId) {
        return addressService.getAddress(addressId);
    }


    @GetMapping("/user/{userId}")
    public List<ResponseAddress> getAllAddresses(@PathVariable String userId) {
        return addressService.getAllAddresses(userId);
    }
}
