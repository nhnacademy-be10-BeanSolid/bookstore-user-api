package com.nhnacademy.bookstoreuserapi.user.controller;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.interfaces.UserControllerDoc;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserService userService;
    private final PointTypeService pointTypeService;

    @PostMapping("/register")
    @Override
    public ResponseEntity<ResponseUser> saveUser(@Valid @RequestBody  UserCreateRequest userCreateRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userCreateRequest));
    }

    @PostMapping("/register/oauth2")
    @Override
    public ResponseEntity<ResponseUser> saveOauth2User(@Valid @RequestBody Oauth2UserCreateRequest request,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveOauth2User(request));
    }

    @GetMapping("/{userId}")
    @Override
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/check-userId")
    @Override
    public boolean existUser(@RequestParam String userId) {
        return userService.isUserExist(userId);
    }

    @GetMapping("/user/{userNo}")
    @Override
    public ResponseEntity<ResponseUser> getUserByUserNo(@PathVariable Long userNo) {

        return ResponseEntity.ok(userService.getUserByUserNo(userNo));
    }

    @GetMapping("/me")
    @Override
    public ResponseEntity<ResponseUser> getUserInfo(@AuthenticatedUserId String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/me/status/WITHDRAWN")
    @Override
    public ResponseEntity<ResponseUser> deleteUser(@AuthenticatedUserId String userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PutMapping("/me/personalinformation")
    @Override
    public ResponseEntity<ResponseUser> updatePersonalInformation(@AuthenticatedUserId String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult){

        return updatePersonalInformationInternal(userId, userUpdateRequest, bindingResult);
    }

    @PutMapping("/{userId}/personalinformation")
    @Override
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
    @Override
    public ResponseEntity<ResponseUser> updateLastLoginAt(@AuthenticatedUserId String userId){
        return ResponseEntity.ok(userService.updateLastLoginAt(userId));
    }

    @PutMapping("/{userNo}/plus-point")
    @Override
    public ResponseEntity<ResponseUser> plusPoint(@PathVariable Long userNo, @RequestParam @Min(0) int point){
        return ResponseEntity.ok(userService.plusPoint(userNo, point));
    }

    @PutMapping("/{userNo}/minus-point")
    @Override
    public ResponseEntity<ResponseUser> minusPoint(@PathVariable Long userNo, @RequestParam @Min(0) int point){
        return ResponseEntity.ok(userService.minusPoint(userNo, point));
    }

    // 현재 내가 가지고 있는 포인트 액수 조회
    @GetMapping("/me/my-point")
    @Override
    public ResponseEntity<Integer> getMyPoint(@AuthenticatedUserId String userId){
        return ResponseEntity.ok(userService.getUserPoint(userId));
    }

    @GetMapping("/{userNo}/my-point")
    @Override
    public ResponseEntity<Integer> getUserPointByUserNo(@PathVariable Long userNo){
        return ResponseEntity.ok(userService.getUserPointByUserNo(userNo));
    }

    @PutMapping("/me/status")
    @Override
    public ResponseEntity<ResponseUser> updateStatus(@AuthenticatedUserId String userId, @RequestParam String status){
        return ResponseEntity.ok(userService.updateUserStatus(userId, status));
    }

    // 전체회원등급 조정api
    @PutMapping("/bulk/grade")
    @Override
    public ResponseEntity<Void> bulkUpdateUserGrades() {
        userService.bulkUpdateUserGrades();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findId")
    @Override
    public ResponseEntity<ResponseUserId> getUserIdByUserNameAndUserEmail(@RequestParam @NotBlank String userName, @RequestParam @NotBlank String userEmail) {
        return ResponseEntity.ok(userService.getUserIdByUserNameAndUserEmail(userName, userEmail));
    }

    @GetMapping("/{userNo}/earning-rate")
    @Override
    public ResponseEntity<ResponsePointType> getEarningRateByUserNo(@PathVariable Long userNo) {
        UserGrade.Grade userGrade = userService.getUserGradeByUserNo(userNo);
        return ResponseEntity.ok(pointTypeService.getEarningRateByGradeNameAndTypeName(userGrade));
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<ResponseUser>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/bulk/status")
    @Override
    public ResponseEntity<Void> bulkUpdateUserStatus() {
        userService.updateDormantUsers();
        return ResponseEntity.ok().build();
    }
}
