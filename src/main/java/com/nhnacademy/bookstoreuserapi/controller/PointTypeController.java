package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.service.PointTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/pointType")
@RequiredArgsConstructor
public class PointTypeController {

    private final PointTypeService pointTypeService;

    @GetMapping
    public List<ResponsePointType> getPointTypeByGradeName(@RequestParam(name = "gradeName", required = false) String gradeName) {

        if(gradeName == null) {
            return pointTypeService.getAllPointTypes();
        }

        return pointTypeService.getPointTypeByGradeName(UserGrade.Grade.valueOf(gradeName));
    }

    @PostMapping
    public void createPointType(@RequestBody PointTypeCreateRequest request) {

        pointTypeService.savePointType(request);
    }

    @DeleteMapping("/{typeId}")
    public void deletePointType(@PathVariable Long typeId) {
        pointTypeService.deletePointType(typeId);
    }

    @PutMapping("/{typeId}/point")
    public ResponsePointType updateEarningPoint(@PathVariable Long typeId, @RequestParam Long point) {

        return pointTypeService.updateEarningPoint(point, typeId);
    }

    @PutMapping("/{typeId}/rate")
    public ResponsePointType updateRatePoint(@PathVariable Long typeId, @RequestParam int rate) {

        return pointTypeService.updateEarningRate(rate, typeId);
    }
}
