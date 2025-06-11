package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
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
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        User user = userService.findById(userId).get();

        return ResponseEntity.ok(new ResponseUser(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformation(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest){

        User user = userService.findById(userId).get();
        user.setUserName(userUpdateRequest.userName());
        user.setUserPassword(userUpdateRequest.userPassword());
        user.setUserBirth(userUpdateRequest.userBirth());
        user.setUserPhoneNumber(userUpdateRequest.userPhoneNumber());
        user.setUserEmail(userUpdateRequest.userEmail());
        userService.updatePersonalInformation(user);

        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/lastloginat")
    public ResponseEntity<ResponseUser> updateLastLoginAt(@PathVariable String userId){

        userService.updateLastLoginAt(userId);

        User user = userService.findById(userId).get();
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseUser> updatePoint(@PathVariable String userId, @RequestParam int point){

        userService.updatePoint(userId, point);

        User user = userService.findById(userId).get();

        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<ResponseUser> updateStatus(@PathVariable String userId, @RequestParam User.Status status){

        userService.updateUserStatus(userId, status);

        User user = userService.findById(userId).get();

        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/ordermoney")     // 기존 주문 금액에 더함 ++orderMoney
    public ResponseEntity<ResponseUser> updateOrderMoney(@PathVariable String userId, @RequestParam long orderMoney) {

        userService.updateOrderMoney(userId, orderMoney);

        User user = userService.findById(userId).get();

        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/grade")
    public ResponseEntity<ResponseUser> updateUserGradeName(@PathVariable String userId, @RequestParam String gradeName) {

        userService.updateUserGradeName(userId, gradeName);

        User user = userService.findById(userId).get();

        return ResponseEntity.ok(new ResponseUser(user));
    }


}
