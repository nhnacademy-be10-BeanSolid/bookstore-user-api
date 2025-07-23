package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.address.domain.AddressCreateRequest;
import com.nhnacademy.bookstoreuserapi.address.domain.ResponseAddress;
import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            @ApiResponse(responseCode = "400", description = "잘못된 요청(유효성 검증 실패, 주소 개수 10개 초과 등)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = {@ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
            {
              "timestamp": "2025-07-23 10:36:56",
              "status": 400,
              "error": "BAD_REQUEST",
              "path": "/users/me/address",
              "message": "User ID must not be blank"
            }
            """
            ),
                    @ExampleObject(
                            name = "AddressLimitExceededExample",
                            summary = "주소 개수 초과 예시",
                            value = """
                            {
                               "timestamp": "2025-07-23 11:05:10",
                               "status": 400,
                               "error": "BAD_REQUEST",
                               "path": "/users/me/address",
                               "message": "User test already has the maximum of 10 addresses."
                            }
            """
                    )
            })),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "NotFoundExample",
                    summary = "사용자 ID가 잘못된 경우 예시",
                    value = """
                            {
                              "timestamp": "2025-07-23 10:44:58",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/users/me/address",
                              "message": "User ID : string not found"
                            }
            """
            ))),
            @ApiResponse(responseCode = "403", description = "권한이 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "ForbiddenExample",
                    summary = "유저 ID 불일치 예시",
                    value = """
                            {
                              "timestamp": "2025-07-23 10:50:00",
                              "status": 403,
                              "error": "FORBIDDEN",
                              "path": "/users/me/address",
                              "message": "요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다."
                            }
            """
            )))
    })
    ResponseEntity<ResponseAddress> addAddress(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @RequestBody AddressCreateRequest address,
            BindingResult bindingResult
    );

    @Operation(summary = "주소 삭제", description = "지정된 ID의 주소를 삭제합니다.")
    @Parameter(name = "addressId", description = "삭제할 주소 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주소 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "NotFoundExample",
                    summary = "주소 ID가 잘못된 경우 예시",
                    value = """
                            {
                                 "timestamp": "2025-07-23 13:30:48",
                                 "status": 404,
                                 "error": "NOT_FOUND",
                                 "path": "/users/me/address/1",
                                 "message": "Address ID 1 not found."
                            }
            """
            ))),
            @ApiResponse(responseCode = "403", description = "권한이 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "ForbiddenExample",
                    summary = "유저 ID 불일치 예시",
                    value = """
                            {
                              "timestamp": "2025-07-23 10:50:00",
                              "status": 403,
                              "error": "FORBIDDEN",
                              "path": "/users/me/address/3",
                              "message": "요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다."
                            }
            """
            )))
    })
    ResponseEntity<Void> deleteAddress(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @PathVariable @Min(1) long addressId
    );

    @Operation(summary = "단일 주소 조회", description = "지정된 ID의 단일 주소 정보를 조회합니다.")
    @Parameter(name = "addressId", description = "조회할 주소 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAddress.class), examples = @ExampleObject(
                    name = "GetAddressExample",
                    summary = "주소 조회 성공 예시",
                    value = """
                            {
                              "addressId": 20,
                              "addressNickName": "wlq11",
                              "addressDetail": "03054 서울 종로구 팔판길 1-6 (팔판동)",
                              "userId": "test"
                            }
                            """
            ))),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "NotFoundExample",
                    summary = "주소 ID가 잘못된 경우 예시",
                    value = """
                            {
                                 "timestamp": "2025-07-23 13:30:48",
                                 "status": 404,
                                 "error": "NOT_FOUND",
                                 "path": "/users/me/address/1",
                                 "message": "Address ID 1 not found."
                            }
            """
            ))),
            @ApiResponse(responseCode = "403", description = "권한이 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "ForbiddenExample",
                    summary = "유저 ID 불일치 예시",
                    value = """
                            {
                              "timestamp": "2025-07-23 10:50:00",
                              "status": 403,
                              "error": "FORBIDDEN",
                              "path": "/users/me/address/3",
                              "message": "요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다."
                            }
            """
            )))
    })
    ResponseEntity<ResponseAddress> getAddress(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @PathVariable @Min(1) long addressId
    );

    @Operation(summary = "모든 주소 조회", description = "현재 사용자의 모든 주소 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 주소 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAddress.class), examples = @ExampleObject(
                    name = "GetAllAddressesExample",
                    summary = "모든 주소 조회 성공 예시",
                    value = """
                            [
                              {
                                "addressId": 5,
                                "addressNickName": "집2",
                                "addressDetail": "61452 광주 동구 조선대길 7 101호 (서석동)",
                                "userId": "test"
                              }
                            ]
                            """
            ))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(유효성 검증 실패 - X-USER-ID 헤더 미존재)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
            {
              "timestamp": "2025-07-23 10:36:56",
              "status": 400,
              "error": "BAD_REQUEST",
              "path": "/users/me/address",
              "message": "User ID must not be blank"
            }
            """
            )))
    })
    ResponseEntity<List<ResponseAddress>> getAllAddresses(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );
}