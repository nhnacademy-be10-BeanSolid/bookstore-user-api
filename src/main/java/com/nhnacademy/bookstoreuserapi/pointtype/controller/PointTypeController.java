package com.nhnacademy.bookstoreuserapi.pointtype.controller;

import com.nhnacademy.bookstoreuserapi.common.controller.interfaces.PointTypeControllerDoc;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users/pointType")
@RequiredArgsConstructor
public class PointTypeController implements PointTypeControllerDoc {

    private final PointTypeService pointTypeService;

    @GetMapping
    @Override
    public ResponseEntity<Page<ResponsePointType>> getPointTypeByGradeName(@RequestParam(name = "gradeName", required = false) @Size(max = 10) String gradeName, Pageable pageable) {

        if(gradeName == null) {
            return ResponseEntity.ok().body(pointTypeService.getAllPointTypes(pageable));
        }

        return ResponseEntity.ok().body(pointTypeService.getPointTypeByGradeName(UserGrade.Grade.valueOf(gradeName), pageable));
    }

    @PostMapping
    @Override
    public ResponseEntity<Void> createPointType(@Valid @RequestBody PointTypeCreateRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        pointTypeService.savePointType(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{typeId}")
    @Override
    public ResponseEntity<Void> deletePointType(@PathVariable @NotNull @Min(1) Long typeId) {
        pointTypeService.deletePointType(typeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{typeId}/point")
    @Override
    public ResponseEntity<ResponsePointType> updateEarningPoint(@PathVariable @NotNull @Min(1) Long typeId, @RequestParam @NotNull @Min(0) int point) {

        return ResponseEntity.ok().body(pointTypeService.updateEarningPoint(point, typeId));
    }

    @PutMapping("/{typeId}/rate")
    @Override
    public ResponseEntity<ResponsePointType> updateRatePoint(@PathVariable @NotNull @Min(1) Long typeId, @RequestParam int rate) {
        return ResponseEntity.ok().body(pointTypeService.updateEarningRate(rate, typeId));
    }
}
