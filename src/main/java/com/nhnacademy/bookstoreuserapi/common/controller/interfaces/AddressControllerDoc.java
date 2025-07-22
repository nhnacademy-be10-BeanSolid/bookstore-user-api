package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.address.domain.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "주소 API", description = "주소에 관한 Controller 입니다.")
public interface AddressControllerDoc {

    @Operation(summary = "주소 추가", description = "새로운 주소를 추가합니다.")
    @RequestBody(
            description = "추가할 주소 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = AddressCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주소 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAddress.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseAddress> addAddress(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody AddressCreateRequest address,
            BindingResult bindingResult
    );

    @Operation(summary = "주소 삭제", description = "지정된 ID의 주소를 삭제합니다.")
    @Parameter(name = "addressId", description = "삭제할 주소 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주소 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Void> deleteAddress(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @PathVariable @Min(1) long addressId
    );

    @Operation(summary = "단일 주소 조회", description = "지정된 ID의 단일 주소 정보를 조회합니다.")
    @Parameter(name = "addressId", description = "조회할 주소 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAddress.class))),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseAddress> getAddress(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @PathVariable @Min(1) long addressId
    );

    @Operation(summary = "모든 주소 조회", description = "현재 사용자의 모든 주소 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 주소 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<List<ResponseAddress>> getAllAddresses(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );
}