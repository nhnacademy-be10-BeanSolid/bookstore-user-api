package com.nhnacademy.bookstoreuserapi.address.controller;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.address.domain.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.common.controller.interfaces.AddressControllerDoc;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.address.service.AddressService;
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
@RequestMapping("/users/me/address")
public class AddressController implements AddressControllerDoc {
    private final AddressService addressService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseAddress> addAddress(@AuthenticatedUserId String userId, @Valid @RequestBody AddressCreateRequest address, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(userId, address));
    }
    @DeleteMapping("/{addressId}")
    @Override
    public ResponseEntity<Void> deleteAddress(@AuthenticatedUserId String userId, @PathVariable @Min(1) long addressId) {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{addressId}")
    @Override
    public ResponseEntity<ResponseAddress> getAddress(@AuthenticatedUserId String userId, @PathVariable @Min(1) long addressId) {
        return ResponseEntity.ok().body(addressService.getAddress(userId, addressId));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ResponseAddress>> getAllAddresses(@AuthenticatedUserId String userId) {
        return ResponseEntity.ok().body(addressService.getAllAddresses(userId));
    }
}
