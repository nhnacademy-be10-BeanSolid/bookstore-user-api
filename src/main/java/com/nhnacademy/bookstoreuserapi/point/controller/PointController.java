package com.nhnacademy.bookstoreuserapi.point.controller;

import com.nhnacademy.bookstoreuserapi.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/me/point")
public class PointController {

    private final PointService pointService;
    @GetMapping
    public ResponseEntity<Page<ResponsePoint>> getPoint(@AuthenticatedUserId String userId, Pageable pageable) {
        return ResponseEntity.ok().body(pointService.findAll(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<ResponsePoint> savePoint(@AuthenticatedUserId String userId, @Valid @RequestBody PointCreateRequest pointCreateRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pointService.savePoint(userId, pointCreateRequest));
    }
}
