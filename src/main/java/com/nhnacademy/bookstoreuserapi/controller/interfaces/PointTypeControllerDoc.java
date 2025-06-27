package com.nhnacademy.bookstoreuserapi.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포인트 타입 API", description = "포인트 타입에 관한 Controller **포인트 타입 = 1 : 회원가입, 2 : 리뷰작성, 유저등급에 따른 순수 결제금액별 포인트 적립**")
public interface PointTypeControllerDoc {
    @Operation(summary = "포인트 타입 조회", description = "포인트 타입을 조회합니다. 필요 파라미터 : gradeName(유저 등급명)")
    @Parameters({
            @Parameter(name = "gradeName", description = "유저 등급명", required = false, example = "BASIC"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
            @Parameter(name = "size", description = "페이지 크기", example = "10"),
            @Parameter(name = "sort", description = "정렬 기준 (예: field,asc 또는 field,desc)", example = "typeId,asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 타입 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePointType.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping
    ResponseEntity<Page<ResponsePointType>> getPointTypeByGradeName(@RequestParam(name = "gradeName", required = false) @Size(max = 10) String gradeName, Pageable pageable);

    @Operation(summary = "포인트 타입 추가", description = "포인트 타입을 추가합니다. 필요 바디 : 타입이름(필수), 획득 포인트, 획득 비율(유저 등급별), 유저 등급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "포인트 타입 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePointType.class))),
            @ApiResponse(responseCode = "409", description = "포인트 타입이 이미 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @RequestBody(
            description = "포인트 타입 생성 요청",
            required = true,
            content = @Content(schema = @Schema(implementation = PointTypeCreateRequest.class))
    )
    @PostMapping
    ResponseEntity<Void> createPointType(@Valid @RequestBody PointTypeCreateRequest request, BindingResult bindingResult);

    @DeleteMapping("/{typeId}")
    ResponseEntity<Void> deletePointType(@PathVariable @NotNull @Min(1) Long typeId);

    @PutMapping("/{typeId}/point")
    ResponseEntity<ResponsePointType> updateEarningPoint(@PathVariable @NotNull @Min(1) Long typeId, @RequestParam @NotNull @Min(0) int point);

    @PutMapping("/{typeId}/rate")
    ResponseEntity<ResponsePointType> updateRatePoint(@PathVariable @NotNull @Min(1) Long typeId, @RequestParam int rate);
}
