package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Tag(name = "포인트 API", description = "포인트에 관한 Controller 입니다.")
public interface PointControllerDoc {

    @Operation(summary = "포인트 내역 조회", description = "현재 인증된 사용자의 포인트 내역을 페이지네이션하여 조회합니다.")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "sort", description = "정렬 기준 (예: pointId,desc)", example = "pointId,desc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 내역 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Page<ResponsePoint>> getPoint(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            Pageable pageable
    );

    @Operation(summary = "포인트 생성", description = "새로운 포인트 내역을 생성합니다.")
    @RequestBody(
            description = "포인트 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = PointCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "포인트 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePoint.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponsePoint> savePoint(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody PointCreateRequest pointCreateRequest,
            BindingResult bindingResult
    );
}