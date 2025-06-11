package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/point")
public class PointController {

    private final PointService pointService;

    @GetMapping("/{userId}")
    public List<ResponsePoint> getPoint(@PathVariable String userId) {

        return pointService.findAll(userId);
    }

    @PostMapping
    public ResponsePoint savePoint(@RequestBody PointCreateRequest pointCreateRequest) {

        return pointService.savePoint(pointCreateRequest);

    }
}
