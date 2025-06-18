package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.exception.InvalidHeaderException;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.PointService;
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
@RequestMapping("/users/point")
public class PointController {

    private final PointService pointService;
    @GetMapping("/me")
    public ResponseEntity<Page<ResponsePoint>> getPoint(@RequestHeader("X-USER-ID") String userId, Pageable pageable) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_BLANK);
        }
        if (userId.length() > 20) {
            throw new InvalidHeaderException(InvalidHeaderException.USER_ID_TOO_LONG);
        }
        return ResponseEntity.ok().body(pointService.findAll(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<ResponsePoint> savePoint(@Valid @RequestBody PointCreateRequest pointCreateRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pointService.savePoint(pointCreateRequest));

    }
}
