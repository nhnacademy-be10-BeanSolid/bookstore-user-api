package com.nhnacademy.bookstoreuserapi.guest.controller;

import com.nhnacademy.bookstoreuserapi.common.controller.interfaces.GuestControllerDoc;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.guest.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/guests")
@RequiredArgsConstructor
public class GuestController implements GuestControllerDoc {

    private final GuestService guestService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseGuest> register(@Valid @RequestBody GuestCreateRequest guest,
                                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        ResponseGuest responseGuest = guestService.addGuest(guest);
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{orderId}")
                .buildAndExpand(responseGuest.getOrderId())
                .toUri();
        return ResponseEntity.created(locationUri).body(responseGuest);
    }

    @GetMapping("/{orderId}")
    @Override
    public ResponseEntity<ResponseGuest> getGuest(@PathVariable Long orderId) {

        return ResponseEntity.ok().body(guestService.getGuest(orderId));
    }

    @DeleteMapping("/{orderId}")
    @Override
    public ResponseEntity<Void> deleteGuest(@PathVariable Long orderId){

        guestService.deleteGuest(orderId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/password")
    @Override
    public ResponseEntity<String> getGuestPassword(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(guestService.getGuestEncodedPassword(orderId));
    }
}
