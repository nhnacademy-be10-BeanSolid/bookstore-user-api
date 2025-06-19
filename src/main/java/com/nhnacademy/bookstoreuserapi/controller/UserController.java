package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
import com.nhnacademy.bookstoreuserapi.service.UserService;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUser> saveUser(@Valid @RequestBody  UserCreateRequest userCreateRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userCreateRequest));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseUser> getUserInfo(@AuthenticatedUserId String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @DeleteMapping
    public ResponseEntity<ResponseUser> deleteUser(@AuthenticatedUserId String userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformation(@AuthenticatedUserId String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok().body(userService.updatePersonalInformation(userId, userUpdateRequest));
    }

    @PutMapping("/me/lastloginat")
    public ResponseEntity<ResponseUser> updateLastLoginAt(@AuthenticatedUserId String userId){
        return ResponseEntity.ok(userService.updateLastLoginAt(userId));
    }

    @PutMapping("/me/point")
    public ResponseEntity<ResponseUser> updatePoint(@AuthenticatedUserId String userId, @RequestParam @Min(0) int point){
        return ResponseEntity.ok(userService.updatePoint(userId, point));
    }

    @PutMapping("/me/status")
    public ResponseEntity<ResponseUser> updateStatus(@AuthenticatedUserId String userId, @RequestParam User.Status status){
        return ResponseEntity.ok(userService.updateUserStatus(userId, status));
    }

    @PutMapping("/me/grade")
    public ResponseEntity<ResponseUser> updateUserGradeName(@AuthenticatedUserId String userId, @RequestParam @NotBlank @Size(max = 10) String gradeName) {
        return ResponseEntity.ok(userService.updateUserGradeName(userId, gradeName));
    }
}
