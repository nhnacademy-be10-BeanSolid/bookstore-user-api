package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.user.domain.Oauth2UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUserId;
import com.nhnacademy.bookstoreuserapi.user.domain.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "사용자 API", description = "사용자에 관한 Controller 입니다.")
public interface UserControllerDoc {

    @Operation(summary = "일반 사용자 회원가입", description = "새로운 일반 사용자를 등록합니다.")
    @RequestBody(
            description = "사용자 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
            examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                            {
                              "userNo": 0,
                              "userId": "유저ID",
                              "userPassword": "암호화된 비밀번호",
                              "userName": "유저 이름",
                              "userPhoneNumber": "000-0000-0000",
                              "userEmail": "aaa@aaa.aaa",
                              "userBirth": "2025-07-24",
                              "userPoint": 0,
                              "userStatus": "ACTIVE",
                              "createdAt": "2025-07-24 07:20:17",
                              "lastLoginAt": "2025-07-24 07:20:17",
                              "userGradeName": "BASIC",
                              "auth": false
                            }
                            """
            ))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(
                            name = "ValidationErrorExample",
                            value = """
                                    {
                                      "timestamp": "발생 시각",
                                      "status": 400,
                                      "error": "BAD_REQUEST",
                                      "path": "users/register",
                                      "message": "상세 오류 메시지",
                                    }
                                    """
                    )))
    })
    ResponseEntity<ResponseUser> saveUser(
            @Valid @RequestBody UserCreateRequest userCreateRequest,
            BindingResult bindingResult
    );

    @Operation(summary = "OAuth2 사용자 회원가입", description = "새로운 OAuth2 사용자를 등록합니다.")
    @RequestBody(
            description = "OAuth2 사용자 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = Oauth2UserCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OAuth2 사용자 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class), examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                            {
                              "userNo": 0,
                              "userId": "PAYCO_123456789",
                              "userPassword": null,
                              "userName": "유저 이름",
                              "userPhoneNumber": "000-0000-0000",
                              "userEmail": "aaa@aaa.aaa",
                              "userBirth": "2025-07-24",
                              "userPoint": 0,
                              "userStatus": "ACTIVE",
                              "createdAt": "2025-07-24 07:20:17",
                              "lastLoginAt": null,
                              "userGradeName": "BASIC",
                              "auth": false
                            }
                            """
            ))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "ValidationErrorExample",
                            value = """
                                    {
                                      "timestamp": "발생 시각",
                                      "status": 400,
                                      "error": "BAD_REQUEST",
                                      "path": "users/register/oauth2",
                                      "message": "상세 오류 메시지",
                                    }
                                    """
                    )))
    })
    ResponseEntity<ResponseUser> saveOauth2User(
            @Valid @RequestBody Oauth2UserCreateRequest request,
            BindingResult bindingResult
    );

    @Operation(summary = "사용자 ID로 사용자 조회", description = "지정된 사용자 ID로 사용자 정보를 조회합니다.")
    @Parameter(name = "userId", description = "조회할 사용자 ID", required = true, example = "test")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
            examples = @ExampleObject(
                            name = "ResponseUserExample",
                            value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "ACTIVE",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
            ))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(
                    name = "UserNotFoundErrorExample",
                    value = """
                            {
                              "timestamp": "2025-07-24 16:50:08",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/users/test",
                              "message": "User ID : test not found"
                            }
                            """
            )))
    })
    ResponseEntity<ResponseUser> getUser(
            @PathVariable String userId
    );

    @Operation(summary = "사용자 ID 존재 여부 확인", description = "지정된 사용자 ID가 존재하는지 확인합니다.")
    @Parameter(name = "userId", description = "확인할 사용자 ID", required = true, example = "test")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "존재 여부 확인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    })
    boolean existUser(
            @RequestParam String userId
    );

    @Operation(summary = "사용자 번호로 사용자 조회", description = "지정된 사용자 번호로 사용자 정보를 조회합니다.")
    @Parameter(name = "userNo", description = "조회할 사용자 번호", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
            examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                            {
                              "userNo": 1,
                              "userId": "admin",
                              "userPassword": "암호화된 비밀번호",
                              "userName": "test",
                              "userPhoneNumber": "010-1234-5555",
                              "userEmail": "admin@test.com",
                              "userBirth": "2025-06-01",
                              "userPoint": 207,
                              "userStatus": "ACTIVE",
                              "createdAt": "2024-06-16 12:34:56",
                              "lastLoginAt": "2025-07-24 15:11:44",
                              "userGradeName": "BASIC",
                              "auth": true
                            }
                            """
                              ))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "UserNotFoundErrorExample",
                            value = """
                                    {
                                      "timestamp": "2025-07-24 16:52:48",
                                      "status": 404,
                                      "error": "NOT_FOUND",
                                      "path": "/users/user/1",
                                      "message": "User No : 1 not found"
                                    }
                            """
                    )))
    })
    ResponseEntity<ResponseUser> getUserByUserNo(
            @PathVariable Long userNo
    );

    @Operation(summary = "인증된 사용자 정보 조회", description = "현재 인증된 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
                    examples = @ExampleObject(
                            name = "ResponseUserExample",
                            value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "ACTIVE",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
                    ))),
    })
    ResponseEntity<ResponseUser> getUserInfo(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "사용자 탈퇴 (상태 변경)", description = "현재 인증된 사용자의 상태를 'WITHDRAWN'으로 변경하여 탈퇴 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 탈퇴 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
            examples = @ExampleObject(
                            name = "ResponseUserExample",
                            value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "WITHDRAWN",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
            ))),
    })
    ResponseEntity<ResponseUser> deleteUser(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "인증된 사용자 개인 정보 수정", description = "현재 인증된 사용자의 개인 정보를 수정합니다.")
    @RequestBody(
            description = "수정할 사용자 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 정보 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
            examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                            {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "WITHDRAWN",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                            }
                            """
            ))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "ValidationErrorExample",
                            value = """
                                    {
                                      "timestamp": "발생 시각",
                                      "status": 400,
                                      "error": "BAD_REQUEST",
                                      "path": "users/me/personalinformation",
                                      "message": "상세 오류 메시지",
                                    }
                                    """
                    ))),
    })
    ResponseEntity<ResponseUser> updatePersonalInformation(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult
    );

    @Operation(summary = "경로 변수를 통한 사용자 개인 정보 수정", description = "지정된 사용자 ID를 통해 개인 정보를 수정합니다.")
    @Parameter(name = "userId", description = "수정할 사용자 ID", required = true, example = "test")
    @RequestBody(
            description = "수정할 사용자 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 정보 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                            {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "WITHDRAWN",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                            }
                            """
            ))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "ValidationErrorExample",
                            value = """
                                    {
                                      "timestamp": "발생 시각",
                                      "status": 400,
                                      "error": "BAD_REQUEST",
                                      "path": "users/{userId}/personalinformation",
                                      "message": "상세 오류 메시지",
                                    }
                                    """
                    ))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "UserNotFoundErrorExample",
                            value = """
                                    {
                                      "timestamp": "2025-07-24 16:52:48",
                                      "status": 404,
                                      "error": "NOT_FOUND",
                                      "path": "/users/test/personalinformation",
                                      "message": "User Id : test not found"
                                    }
                            """
                    )))
    })
    ResponseEntity<ResponseUser> updatePersonalInformationPathVariable(
            @PathVariable String userId,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult
    );

    @Operation(summary = "마지막 로그인 시간 업데이트", description = "현재 인증된 사용자의 마지막 로그인 시간을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마지막 로그인 시간 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class), examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "ACTIVE",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
            ))),
    })
    ResponseEntity<ResponseUser> updateLastLoginAt(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "사용자 포인트 증가", description = "지정된 사용자 번호의 포인트를 증가시킵니다.")
    @Parameter(name = "userNo", description = "포인트를 증가시킬 사용자 번호", required = true, example = "1")
    @Parameter(name = "point", description = "증가시킬 포인트 양", required = true, example = "100")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 증가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),
            examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "ACTIVE",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
            ))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "UserNotFoundErrorExample",
                            value = """
                                    {
                                              "timestamp": "2025-07-24 18:01:21",
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "path": "/users/13333/plus-point",
                                              "message": "User ID : null not found"
                                    }
                            """
                    )))
    })
    ResponseEntity<ResponseUser> plusPoint(
            @PathVariable Long userNo, @RequestParam @Min(0) int point
    );

    @Operation(summary = "사용자 포인트 감소", description = "지정된 사용자 번호의 포인트를 감소시킵니다.")
    @Parameter(name = "userNo", description = "포인트를 감소시킬 사용자 번호", required = true, example = "1")
    @Parameter(name = "point", description = "감소시킬 포인트 양", required = true, example = "50")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 감소 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class),examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "ACTIVE",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
            ))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "UserNotFoundErrorExample",
                            value = """
                                    {
                                              "timestamp": "2025-07-24 18:01:21",
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "path": "/users/13333/minus-point",
                                              "message": "User ID : null not found"
                                    }
                            """
                    )))
    })
    ResponseEntity<ResponseUser> minusPoint(
            @PathVariable Long userNo, @RequestParam @Min(0) int point
    );

    @Operation(summary = "현재 사용자 포인트 조회", description = "현재 인증된 사용자의 보유 포인트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(유효성 검증 실패 - X-USER-ID 헤더 미존재)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
                            {
                              "timestamp": "2025-07-24 18:03:43",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "path": "/users/me/my-point",
                              "message": "User ID must not be blank"
                            }
                            """
            )))
    })
    ResponseEntity<Integer> getMyPoint(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "사용자 번호로 포인트 조회", description = "지정된 사용자 번호의 보유 포인트를 조회합니다.")
    @Parameter(name = "userNo", description = "포인트를 조회할 사용자 번호", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "UserNotFoundErrorExample",
                            value = """
                                    {
                                              "timestamp": "2025-07-24 18:05:36",
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "path": "/users/134123/my-point",
                                              "message": "User No : 134123 not found"
                                    }
                            """
                    )))
    })
    ResponseEntity<Integer> getUserPointByUserNo(
            @PathVariable Long userNo
    );

    @Operation(summary = "사용자 상태 업데이트", description = "현재 인증된 사용자의 상태를 업데이트합니다.")
    @Parameter(name = "status", description = "변경할 사용자 상태", required = true, example = "ACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 상태 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class), examples = @ExampleObject(
                    name = "ResponseUserExample",
                    value = """
                                    {
                                      "userNo": 4,
                                      "userId": "test",
                                      "userPassword": "암호화된 비밀번호",
                                      "userName": "testedit",
                                      "userPhoneNumber": "010-1234-5679",
                                      "userEmail": "test@test.edit",
                                      "userBirth": "2000-09-02",
                                      "userPoint": 10000,
                                      "userStatus": "ACTIVE",
                                      "createdAt": "2024-06-16 12:34:56",
                                      "lastLoginAt": "2025-07-23 10:35:44",
                                      "userGradeName": "GOLD",
                                      "auth": false
                                    }
                                    """
            ))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(유효성 검증 실패 - X-USER-ID 헤더 미존재)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
                            {
                              "timestamp": "2025-07-24 18:03:43",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "path": "/users/me/status",
                              "message": "User ID must not be blank"
                            }
                            """
            )))
    })
    ResponseEntity<ResponseUser> updateStatus(
            @Parameter(hidden = true) @AuthenticatedUserId String userId, @RequestParam String status
    );

    @Operation(summary = "전체 회원 등급 일괄 조정", description = "모든 사용자의 등급을 일괄적으로 조정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 등급 일괄 조정 성공")
    })
    ResponseEntity<Void> bulkUpdateUserGrades();

    @Operation(summary = "사용자 ID 찾기", description = "사용자 이름과 이메일로 사용자 ID를 찾습니다.")
    @Parameter(name = "userName", description = "사용자 이름", required = true, example = "홍길동")
    @Parameter(name = "userEmail", description = "사용자 이메일", required = true, example = "test@example.com")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 ID 찾기 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserId.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),examples = @ExampleObject(
                    name = "UserNotFoundErrorExample",
                    value = """
                            {
                              "timestamp": "2025-07-24 18:08:58",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/users/findId",
                              "message": "User Name : 홍길동 Email : test@example.com not found"
                            }
                            """
            )))
    })
    ResponseEntity<ResponseUserId> getUserIdByUserNameAndUserEmail(
            @RequestParam @NotBlank String userName, @RequestParam @NotBlank String userEmail
    );

    @Operation(summary = "사용자 번호로 적립률 조회", description = "지정된 사용자 번호의 등급에 따른 적립률을 조회합니다.")
    @Parameter(name = "userNo", description = "적립률을 조회할 사용자 번호", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "적립률 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePointType.class), examples = @ExampleObject(
                    name = "ResponsePointTypeExample",
                    value = """
                            {
                              "typeId": 3,
                              "typeName": "순수금액별 포인트 적립(BASIC)",
                              "earningPoint": 0,
                              "earningRate": 1,
                              "gradeName": "BASIC",
                              "isActive": true
                            }
                            """
            ))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(
                            name = "UserNotFoundErrorExample",
                            value = """
                                    {
                                              "timestamp": "2025-07-24 18:10:32",
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "path": "/users/132423/earning-rate",
                                              "message": "User No : 132423 not found"
                                    }
                            """
                    )))
    })
    ResponseEntity<ResponsePointType> getEarningRateByUserNo(
            @PathVariable Long userNo
    );

    @Operation(summary = "모든 사용자 조회", description = "모든 사용자 정보를 페이지네이션하여 조회합니다.")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "sort", description = "정렬 기준 (예: userId,asc)", example = "userId,asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    ResponseEntity<Page<ResponseUser>> getAllUsers(Pageable pageable);

    @Operation(summary = "휴면 사용자 상태 일괄 업데이트", description = "휴면 상태인 사용자들의 상태를 일괄적으로 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "휴면 사용자 상태 일괄 업데이트 성공")
    })
    ResponseEntity<Void> bulkUpdateUserStatus();
}