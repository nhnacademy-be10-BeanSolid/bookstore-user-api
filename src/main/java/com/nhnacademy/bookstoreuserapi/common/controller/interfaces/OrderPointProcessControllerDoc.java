package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request.OrderPointPlusProcessRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "주문 포인트 처리 API", description = "주문 시 포인트 증감 처리에 관한 Controller 입니다.")
public interface OrderPointProcessControllerDoc {

    @Operation(summary = "주문 포인트 증가", description = "주문 시 사용자 ���인트를 증가시킵니다.")
    @Parameter(name = "userNo", description = "사용자 ID", required = true, example = "1")
    @RequestBody(
            description = "포인트 증가 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = OrderPointPlusProcessRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 증가 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Void> orderPointPlus(
            @PathVariable Long userNo,
            @Valid @org.springframework.web.bind.annotation.RequestBody OrderPointPlusProcessRequest request,
            BindingResult bindingResult
    );

    @Operation(summary = "주문 포인트 감소", description = "주문 시 사용자 포인트를 감소시킵니다.")
    @Parameter(name = "userNo", description = "사용자 ID", required = true, example = "1")
    @RequestBody(
            description = "포인트 감소 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = OrderPointMinusProcessRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 감소 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Void> orderPointMinus(
            @PathVariable Long userNo,
            @Valid @org.springframework.web.bind.annotation.RequestBody OrderPointMinusProcessRequest request,
            BindingResult bindingResult
    );
}