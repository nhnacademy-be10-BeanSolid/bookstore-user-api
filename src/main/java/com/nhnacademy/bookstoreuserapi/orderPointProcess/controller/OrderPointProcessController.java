package com.nhnacademy.bookstoreuserapi.orderPointProcess.controller;

import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.service.OrderPointProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/order-point")
@RequiredArgsConstructor
public class OrderPointProcessController {

    private final OrderPointProcessService orderPointProcessService;

    @PostMapping("/{userNo}/plus")
    public ResponseEntity<Void> orderPointPlus(@PathVariable Long userNo,
                                               @Valid @RequestBody OrderPointPlusProcessRequest request,
                                               BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        orderPointProcessService.orderPointPlusProcess(userNo, request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userNo}/minus")
    public ResponseEntity<Void> orderPointMinus(@PathVariable Long userNo,
                                                @Valid @RequestBody OrderPointMinusProcessRequest request,
                                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        orderPointProcessService.orderPointMinusProcess(userNo, request);

        return ResponseEntity.ok().build();
    }
}
