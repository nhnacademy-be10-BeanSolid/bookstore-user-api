package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포인트 타입 API", description = "포인트 타입에 관한 Controller 입니다. **포인트 타입 = 1 : 회원가입, 2 : 리뷰작성, 유저등급에 따른 순수 결제금액별 포인트 적립**")
public interface PointTypeControllerDoc {
    @Operation(summary = "포인트 타입 목록 조회", description = "포인트 타입 목록을 조회합니다. 'gradeName' 파라미터가 없으면 전체 목록을, 있으면 해당 등급의 목록을 조회합니다.")
    @Parameter(name = "gradeName", description = "유저 등급명 (예: BASIC, ROYAL, GOLD, PLATINUM)", example = "BASIC")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "sort", description = "정렬 기준 (예: typeId,asc)", example = "typeId,asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 타입 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping
    ResponseEntity<Page<ResponsePointType>> getPointTypeByGradeName(@RequestParam(name = "gradeName", required = false) @Size(max = 10) String gradeName, Pageable pageable);


    @Operation(summary = "포인트 타입 생성", description = "새로운 포인트 타입을 생성합니다.")
    @RequestBody(
            description = "포인트 타입 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = PointTypeCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "포인트 타입 생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping
    ResponseEntity<Void> createPointType(@Valid @RequestBody PointTypeCreateRequest request, BindingResult bindingResult);


    @Operation(summary = "포인트 타입 삭제", description = "기존 포인트 타입을 삭제합니다.")
    @Parameter(name = "typeId", description = "삭제할 포인트 타입 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "포인트 ��입 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 포인트 타입", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @DeleteMapping("/{typeId}")
    ResponseEntity<Void> deletePointType(@PathVariable @NotNull @Min(1) Long typeId);

    @Operation(summary = "포인트 타입 활성/비활성 상태 변경", description = "특정 포인트 타입의 활성(isActive) 상태를 토글합니다.")
    @Parameter(name = "typeId", description = "상태를 변경할 포인트 타입 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 포인트 타입", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PutMapping("/{typeId}/isActive")
    void updateIsActive(@PathVariable @NotNull @Min(1) Long typeId);

    @Operation(summary = "포인트 타입 활성 상태 조회", description = "특정 포인트 타입의 현재 활성(isActive) 상태를 조회합니다.")
    @Parameter(name = "typeId", description = "상태를 조회할 포인트 타입 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 포인트 타입", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{typeId}/isActive")
    boolean getIsActive(@PathVariable @NotNull @Min(1) Long typeId);

    @Operation(summary = "포인트 타입 정보 수정", description = "기존 포인트 타입의 정보를 수정합니다.")
    @Parameter(name = "typeId", description = "수정할 포인트 타입 ID", required = true, example = "1")
    @RequestBody(
            description = "포인트 타입 수정 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = PointTypeUpdateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 포인트 타입", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PutMapping("/{typeId}/edit")
    void updatePointType(@Valid @RequestBody PointTypeUpdateRequest request, BindingResult bindingResult, @PathVariable @NotNull @Min(1) Long typeId);

    @Operation(summary = "단일 포인트 타입 조회", description = "ID로 특정 포인트 타입의 상세 정보를 조회합니다.")
    @Parameter(name = "typeId", description = "조회할 포인트 타입 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePointType.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 포인트 타입", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{typeId}")
    ResponseEntity<ResponsePointType> getPointType(@PathVariable @NotNull @Min(1) Long typeId);
}

