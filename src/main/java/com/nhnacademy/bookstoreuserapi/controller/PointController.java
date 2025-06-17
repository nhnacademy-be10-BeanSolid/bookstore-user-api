package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.PointService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @GetMapping("/{userId}")
    public ResponseEntity<Page<ResponsePoint>> getPoint(@PathVariable @NotBlank @Size(max = 20) String userId, Pageable pageable) {

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
