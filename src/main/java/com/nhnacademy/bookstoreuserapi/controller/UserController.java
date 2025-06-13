package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
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
    public ResponseEntity<ResponseUser> saveUser(@Valid @RequestBody  UserCreateRequest userCreateRequest, BindingResult bindingResult) {

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

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseUser> deleteUser(@PathVariable @NotBlank @Size(max = 20) String userId) {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformation(@PathVariable @NotBlank @Size(max = 20) String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setUserName(userUpdateRequest.userName());
        user.setUserPassword(userUpdateRequest.userPassword());
        user.setUserBirth(userUpdateRequest.userBirth());
        user.setUserPhoneNumber(userUpdateRequest.userPhoneNumber());
        user.setUserEmail(userUpdateRequest.userEmail());
        userService.updatePersonalInformation(user);

        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/lastloginat")
    public ResponseEntity<ResponseUser> updateLastLoginAt(@PathVariable @NotBlank @Size(max = 20) String userId){

        userService.updateLastLoginAt(userId);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseUser> updatePoint(@PathVariable @NotBlank @Size(max = 20) String userId, @RequestParam @Min(0) int point){

        userService.updatePoint(userId, point);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<ResponseUser> updateStatus(@PathVariable @NotBlank @Size(max = 20) String userId, @RequestParam User.Status status){

        userService.updateUserStatus(userId, status);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/{userId}/grade")
    public ResponseEntity<ResponseUser> updateUserGradeName(@PathVariable @NotBlank @Size(max = 20) String userId, @RequestParam @NotBlank @Size(max = 10) String gradeName) {

        userService.updateUserGradeName(userId, gradeName);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }


}
