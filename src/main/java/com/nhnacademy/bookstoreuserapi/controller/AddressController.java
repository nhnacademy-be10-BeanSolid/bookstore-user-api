package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/address")
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseAddress addAddress(@Valid @RequestBody AddressCreateRequest address, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return addressService.save(address);
    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable @Min(1) long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{addressId}")
    public ResponseAddress getAddress(@PathVariable @Min(1) long addressId) {
        return addressService.getAddress(addressId);
    }

    @GetMapping("/user/{userId}")
    public List<ResponseAddress> getAllAddresses(@PathVariable @NotBlank @Size(max = 20) String userId) {
        return addressService.getAllAddresses(userId);
    }
}
