package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestAddress;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.service.impl.AddressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/address")
public class AddressController {
    private final AddressServiceImpl addressService;

    @PostMapping
    public ResponseAddress addAddress(@RequestBody SignUpRequestAddress address){
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
