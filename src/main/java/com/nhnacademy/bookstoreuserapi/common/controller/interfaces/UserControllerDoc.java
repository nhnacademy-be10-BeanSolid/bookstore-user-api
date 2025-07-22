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
            @ApiResponse(responseCode = "201", description = "사용자 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> saveUser(
            @Valid @org.springframework.web.bind.annotation.RequestBody UserCreateRequest userCreateRequest,
            BindingResult bindingResult
    );

    @Operation(summary = "OAuth2 사용자 회원가입", description = "새로운 OAuth2 사용자를 등록합니다.")
    @RequestBody(
            description = "OAuth2 사용자 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = Oauth2UserCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OAuth2 사용자 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> saveOauth2User(
            @Valid @org.springframework.web.bind.annotation.RequestBody Oauth2UserCreateRequest request,
            BindingResult bindingResult
    );

    @Operation(summary = "사용자 ID로 사용자 조회", description = "지정된 사용자 ID로 사용자 정보를 조회합니다.")
    @Parameter(name = "userId", description = "조회할 사용자 ID", required = true, example = "testuser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> getUser(
            @PathVariable String userId
    );

    @Operation(summary = "사용자 ID 존재 여부 확인", description = "지정된 사용자 ID가 존재하는지 확인합니다.")
    @Parameter(name = "userId", description = "확인할 사용자 ID", required = true, example = "testuser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "존재 여부 확인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    })
    boolean existUser(
            @RequestParam String userId
    );

    @Operation(summary = "사용자 번호로 사용자 조회", description = "지정된 사용자 번호로 사용자 정보를 조회합니다.")
    @Parameter(name = "userNo", description = "조회할 사용자 번호", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> getUserByUserNo(
            @PathVariable Long userNo
    );

    @Operation(summary = "인증된 사용자 정보 조회", description = "현재 인증된 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> getUserInfo(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "사용자 탈퇴 (상태 변경)", description = "현재 인증된 사용자의 상태를 'WITHDRAWN'으로 변경하여 탈퇴 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 탈퇴 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
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
            @ApiResponse(responseCode = "200", description = "개인 정보 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> updatePersonalInformation(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult
    );

    @Operation(summary = "경로 변수를 통한 사용자 개인 정보 수정", description = "지정된 사용자 ID를 통해 개인 정보를 수정합니다.")
    @Parameter(name = "userId", description = "수정할 사용자 ID", required = true, example = "testuser")
    @RequestBody(
            description = "수정할 사용자 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 정보 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> updatePersonalInformationPathVariable(
            @PathVariable String userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult
    );

    @Operation(summary = "마지막 로그인 시간 업데이트", description = "현재 인증된 사용자의 마지막 로그인 시간을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마지막 로그인 시간 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> updateLastLoginAt(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "사용자 포인트 증가", description = "지정된 사용자 번호의 포인트를 증가시킵니다.")
    @Parameter(name = "userNo", description = "포인트를 증가시킬 사용자 번호", required = true, example = "1")
    @Parameter(name = "point", description = "증가시킬 포인트 양", required = true, example = "100")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 증가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> plusPoint(
            @PathVariable Long userNo, @RequestParam @Min(0) int point
    );

    @Operation(summary = "사용자 포인트 감소", description = "지정된 사용자 번호의 포인트를 감소시킵니다.")
    @Parameter(name = "userNo", description = "포인트를 감소시킬 사용자 번호", required = true, example = "1")
    @Parameter(name = "point", description = "감소시킬 포인트 양", required = true, example = "50")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 감소 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUser> minusPoint(
            @PathVariable Long userNo, @RequestParam @Min(0) int point
    );

    @Operation(summary = "현재 사용자 포인트 조회", description = "현재 인증된 사용자의 보유 포인트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Integer> getMyPoint(
            @Parameter(hidden = true) @AuthenticatedUserId String userId
    );

    @Operation(summary = "사용자 번호로 포인트 조회", description = "지정된 사용자 번��의 보유 포인트를 조회합니다.")
    @Parameter(name = "userNo", description = "포인트를 조회할 사용자 번호", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Integer> getUserPointByUserNo(
            @PathVariable Long userNo
    );

    @Operation(summary = "사용자 상태 업데이트", description = "현재 인증된 사용자의 상태를 업데이트합니다.")
    @Parameter(name = "status", description = "변경할 사용자 상태", required = true, example = "ACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 상태 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUser.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
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
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseUserId> getUserIdByUserNameAndUserEmail(
            @RequestParam @NotBlank String userName, @RequestParam @NotBlank String userEmail
    );

    @Operation(summary = "사용자 번호로 적립률 조회", description = "지정된 사용자 번호의 등급에 따른 적립률을 조회합니다.")
    @Parameter(name = "userNo", description = "적립률을 조회할 사용자 번호", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "적립률 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePointType.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
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