package com.nhnacademy.bookstoreuserapi.guest.controller;

import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;
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

    @PostMapping
    public ResponseEntity<ResponseGuest> register(@Valid @RequestBody GuestCreateRequest guest,
                                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok().body(guestService.addGuest(guest));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseGuest> getGuest(@PathVariable Long orderId) {

        return ResponseEntity.ok().body(guestService.getGuest(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long orderId){

        guestService.deleteGuest(orderId);

        return ResponseEntity.noContent().build();
    }
}
