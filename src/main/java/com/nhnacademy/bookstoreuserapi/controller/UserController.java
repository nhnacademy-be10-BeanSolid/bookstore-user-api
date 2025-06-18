package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
import com.nhnacademy.bookstoreuserapi.exception.InvalidHeaderException;
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
import org.springframework.http.HttpStatus;
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

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseUser> getUserInfo(@RequestHeader("X-USER-ID") String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
        }
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @DeleteMapping
    public ResponseEntity<ResponseUser> deleteUser(@RequestHeader("X-USER-ID") String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
        }
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/personalinformation")
    public ResponseEntity<ResponseUser> updatePersonalInformation(@RequestHeader("X-USER-ID") String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
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

    @PutMapping("/me/lastloginat")
    public ResponseEntity<ResponseUser> updateLastLoginAt(@RequestHeader("X-USER-ID") String userId){

        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
        }

        userService.updateLastLoginAt(userId);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/me/point")
    public ResponseEntity<ResponseUser> updatePoint(@RequestHeader("X-USER-ID") String userId, @RequestParam @Min(0) int point){

        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
        }
        userService.updatePoint(userId, point);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/me/status")
    public ResponseEntity<ResponseUser> updateStatus(@RequestHeader("X-USER-ID") String userId, @RequestParam User.Status status){

        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
        }
        userService.updateUserStatus(userId, status);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }

    @PutMapping("/me/grade")
    public ResponseEntity<ResponseUser> updateUserGradeName(@RequestHeader("X-USER-ID") String userId, @RequestParam @NotBlank @Size(max = 10) String gradeName) {

        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException("User ID must not be null or blank");
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException("User ID must not exceed 20 characters");
        }
        userService.updateUserGradeName(userId, gradeName);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(new ResponseUser(user));
    }


}
