package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.domain.request.Oauth2UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserId;
import com.nhnacademy.bookstoreuserapi.service.UserService;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping("/register/oauth2")
    public ResponseEntity<ResponseUser> saveOauth2User(@Valid @RequestBody Oauth2UserCreateRequest request,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveOauth2User(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/user/{userNo}")
    public ResponseEntity<ResponseUser> getUserByUserNo(@PathVariable Long userNo) {

        return ResponseEntity.ok(userService.getUserByUserNo(userNo));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseUser> getUserInfo(@AuthenticatedUserId String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/me/status/WITHDRAWN")
    public ResponseEntity<ResponseUser> deleteUser(@AuthenticatedUserId String userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PutMapping("/me/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformation(@AuthenticatedUserId String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok().body(userService.updatePersonalInformation(userId, userUpdateRequest));
    }

    @PutMapping("/{userId}/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformationPathVariable(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){
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

    @GetMapping("/findId")
    public ResponseEntity<ResponseUserId> getUserIdByUserNameAndUserEmail(@Parameter @NotBlank String userName, @Parameter @NotBlank String userEmail) {
        return ResponseEntity.ok(userService.getUserIdByUserNameAndUserEmail(userName, userEmail));
    }
}
