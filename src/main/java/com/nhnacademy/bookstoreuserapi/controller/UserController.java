package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.service.UserService;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity saveUser(@Valid @RequestBody  UserCreateRequest userCreateRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        String userId = userCreateRequest.userId();
        String userPassword = userCreateRequest.userPassword();
        String username = userCreateRequest.userName();
        String userPhoneNumber = userCreateRequest.userPhoneNumber();
        String userEmail = userCreateRequest.userEmail();
        LocalDate userBirth = userCreateRequest.userBirth();

        User user = new User(userId, userPassword, username, userPhoneNumber, userEmail, userBirth);

        userService.saveUser(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {

        User user = userService.findById(userId).get();

        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/personalinformation")
    public ResponseEntity<User> updatePersonalInformation(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest){

        User user = userService.findById(userId).get();
        user.setUserName(userUpdateRequest.userName());
        user.setUserPassword(userUpdateRequest.userPassword());
        user.setUserBirth(userUpdateRequest.userBirth());
        user.setUserPhoneNumber(userUpdateRequest.userPhoneNumber());
        user.setUserEmail(userUpdateRequest.userEmail());
        userService.updatePersonalInformation(user);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{userId}/lastloginat")
    public ResponseEntity<User> updateLastLoginAt(@PathVariable String userId){

        userService.updateLastLoginAt(userId);

        return ResponseEntity.ok().body(userService.findById(userId).get());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updatePoint(@PathVariable String userId, @RequestParam int point){

        userService.updatePoint(userId, point);

        return ResponseEntity.ok().body(userService.findById(userId).get());
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<User> updateStatus(@PathVariable String userId, @RequestParam User.Status status){

        userService.updateUserStatus(userId, status);

        return ResponseEntity.ok().body(userService.findById(userId).get());
    }



}
