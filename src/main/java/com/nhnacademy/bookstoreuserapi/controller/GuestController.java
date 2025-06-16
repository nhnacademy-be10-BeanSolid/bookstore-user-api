package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping("/register")
    public ResponseEntity<ResponseGuest> register(@Valid @RequestBody GuestCreateRequest guest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok().body(guestService.addGuest(guest));
    }
}
