package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;
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

@Tag(name = "비회원 API", description = "비회원 주문에 관한 Controller 입니다.")
public interface GuestControllerDoc {

    @Operation(summary = "비회원 주문 등록", description = "새로운 비회원 주문을 등록합니다.")
    @RequestBody(
            description = "비회원 주문 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = GuestCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "비회원 주문 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGuest.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseGuest> register(
            @Valid @org.springframework.web.bind.annotation.RequestBody GuestCreateRequest guest,
            BindingResult bindingResult
    );

    @Operation(summary = "비회원 주문 조회", description = "지정된 주문 ID로 비회원 주문 정보를 조회합니다.")
    @Parameter(name = "orderId", description = "조회할 주문 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비회원 주문 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGuest.class))),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseGuest> getGuest(
            @PathVariable Long orderId
    );

    @Operation(summary = "비회원 주문 삭제", description = "지정된 주문 ID의 비회원 주문을 삭제합니다.")
    @Parameter(name = "orderId", description = "삭제할 주문 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "비회원 주문 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Void> deleteGuest(
            @PathVariable Long orderId
    );

    @Operation(summary = "비회원 주문 비밀번호 조회", description = "지정된 주문 ID의 비회원 주문 비밀번호를 조회합니다.")
    @Parameter(name = "orderId", description = "비밀번호를 조회할 주문 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<String> getGuestPassword(
            @PathVariable Long orderId
    );
}