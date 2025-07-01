package com.nhnacademy.bookstoreuserapi.guest.controller;

import com.nhnacademy.bookstoreuserapi.guest.domain.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.guest.service.GuestService;
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

    @GetMapping("/{guestEmail}")
    public ResponseEntity<ResponseGuest> getGuest(@PathVariable String guestEmail) {

        return ResponseEntity.ok().body(guestService.getGuest(guestEmail));
    }

    @DeleteMapping("/{guestEmail}")
    public ResponseEntity<String> deleteGuest(@PathVariable String guestEmail){

        guestService.deleteGuest(guestEmail);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestEmail}")
    public ResponseEntity<ResponseGuest> updateGuest(@PathVariable String guestEmail, @Valid @RequestBody GuestUpdateRequest guestUpdateRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok().body(guestService.updateGuest(guestEmail, guestUpdateRequest));
    }
}
