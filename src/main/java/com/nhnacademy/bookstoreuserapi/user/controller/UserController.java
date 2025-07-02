package com.nhnacademy.bookstoreuserapi.user.controller;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.user.domain.Oauth2UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUserId;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.domain.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
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

    @GetMapping("/check-userId")
    public boolean existUser(@RequestParam String userId) {
        return userService.isUserExist(userId);
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

        return updatePersonalInformationInternal(userId, userUpdateRequest, bindingResult);
    }

    @PutMapping("/{userId}/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformationPathVariable(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){

        return updatePersonalInformationInternal(userId, userUpdateRequest, bindingResult);
    }

    // sonarqube 관련 이슈를 해결하기 위해 공통 updatePersonalInformation 와 updatePersonalInformationPathVariable 의 공통 로직을 private 으로 별개의 메소드로 정의
    private ResponseEntity<ResponseUser> updatePersonalInformationInternal(
            String userId, UserUpdateRequest userUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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
    public ResponseEntity<ResponseUserId> getUserIdByUserNameAndUserEmail(@RequestParam @NotBlank String userName, @RequestParam @NotBlank String userEmail) {
        return ResponseEntity.ok(userService.getUserIdByUserNameAndUserEmail(userName, userEmail));
    }
}
