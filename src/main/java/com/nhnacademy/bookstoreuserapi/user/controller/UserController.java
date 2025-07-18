package com.nhnacademy.bookstoreuserapi.user.controller;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.user.domain.*;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    private final PointTypeService pointTypeService;

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

    @PutMapping("/{userNo}/plus-point")
    public ResponseEntity<ResponseUser> plusPoint(@PathVariable Long userNo, @RequestParam @Min(0) int point){
        return ResponseEntity.ok(userService.plusPoint(userNo, point));
    }

    @PutMapping("/{userNo}/minus-point")
    public ResponseEntity<ResponseUser> minusPoint(@PathVariable Long userNo, @RequestParam @Min(0) int point){
        return ResponseEntity.ok(userService.minusPoint(userNo, point));
    }

    // 현재 내가 가지고 있는 포인트 액수 조회
    @GetMapping("/me/my-point")
    public ResponseEntity<Integer> getMyPoint(@AuthenticatedUserId String userId){
        return ResponseEntity.ok(userService.getUserPoint(userId));
    }

    @GetMapping("/{userNo}/my-point")
    public ResponseEntity<Integer> getUserPointByUserNo(@PathVariable Long userNo){
        return ResponseEntity.ok(userService.getUserPointByUserNo(userNo));
    }

    @PutMapping("/me/status")
    public ResponseEntity<ResponseUser> updateStatus(@AuthenticatedUserId String userId, @RequestParam String status){
        return ResponseEntity.ok(userService.updateUserStatus(userId, status));
    }

    // 전체회원등급 조정api
    @PutMapping("/bulk/grade")
    public ResponseEntity<Void> bulkUpdateUserGrades() {
        userService.bulkUpdateUserGrades();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findId")
    public ResponseEntity<ResponseUserId> getUserIdByUserNameAndUserEmail(@RequestParam @NotBlank String userName, @RequestParam @NotBlank String userEmail) {
        return ResponseEntity.ok(userService.getUserIdByUserNameAndUserEmail(userName, userEmail));
    }

    @GetMapping("/{userNo}/earning-rate")
    public ResponseEntity<ResponsePointType> getEarningRateByUserNo(@PathVariable Long userNo) {
        UserGrade.Grade userGrade = userService.getUserGradeByUserNo(userNo);
        return ResponseEntity.ok(pointTypeService.getEarningRateByGradeNameAndTypeName(userGrade));
    }
}
