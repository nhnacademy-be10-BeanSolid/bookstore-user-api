package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.exception.InvalidHeaderException;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseAddress> addAddress(@Valid @RequestBody AddressCreateRequest address, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(address));
    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable @Min(1) long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ResponseAddress> getAddress(@PathVariable @Min(1) long addressId) {
        return ResponseEntity.ok().body(addressService.getAddress(addressId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ResponseAddress>> getAllAddresses(@RequestHeader("X-USER-ID") String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_BLANK);
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_TOO_LONG);
        }
        return ResponseEntity.ok().body(addressService.getAllAddresses(userId));
    }
}
