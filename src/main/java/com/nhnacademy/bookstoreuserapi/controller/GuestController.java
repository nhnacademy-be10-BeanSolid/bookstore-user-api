package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.GuestUpdateRequest;
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

    @GetMapping("/{guestEmail}")
    public ResponseEntity<ResponseGuest> getGuest(@PathVariable String guestEmail) {

        return ResponseEntity.ok().body(guestService.getGuest(guestEmail));
    }

    @DeleteMapping("/{guestEmail}")
    public ResponseEntity<String> deleteGuest(@PathVariable String guestEmail){

        guestService.deleteGuest(guestEmail);

        return ResponseEntity.ok().body("성공적으로 삭제하였습니다.");
    }

    @PutMapping("/{guestEmail}")
    public ResponseEntity<ResponseGuest> updateGuest(@PathVariable String guestEmail, @Valid @RequestBody GuestUpdateRequest guestUpdateRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok().body(guestService.updateGuest(guestEmail, guestUpdateRequest));
    }
}
