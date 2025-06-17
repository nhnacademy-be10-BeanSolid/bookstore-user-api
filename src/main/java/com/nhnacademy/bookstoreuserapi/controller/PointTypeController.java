package com.nhnacademy.bookstoreuserapi.controller;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.PointTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/pointType")
@RequiredArgsConstructor
public class PointTypeController {

    private final PointTypeService pointTypeService;
    @GetMapping
    public List<ResponsePointType> getPointTypeByGradeName(@RequestParam(name = "gradeName", required = false) @Size(max = 10) String gradeName) {

        if(gradeName == null) {
            return pointTypeService.getAllPointTypes();
        }

        return pointTypeService.getPointTypeByGradeName(UserGrade.Grade.valueOf(gradeName));
    }

    @PostMapping
    public void createPointType(@Valid @RequestBody PointTypeCreateRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        pointTypeService.savePointType(request);
    }
    @DeleteMapping("/{typeId}")
    public void deletePointType(@PathVariable @NotNull @Min(1) Long typeId) {
        pointTypeService.deletePointType(typeId);
    }
    @PutMapping("/{typeId}/point")
    public ResponsePointType updateEarningPoint(@PathVariable @NotNull @Min(1) Long typeId, @RequestParam @NotNull @Min(0) Long point) {

        return pointTypeService.updateEarningPoint(point, typeId);
    }
    @PutMapping("/{typeId}/rate")
    public ResponsePointType updateRatePoint(@PathVariable @NotNull @Min(1) Long typeId, @RequestParam int rate) {

        return pointTypeService.updateEarningRate(rate, typeId);
    }
}
