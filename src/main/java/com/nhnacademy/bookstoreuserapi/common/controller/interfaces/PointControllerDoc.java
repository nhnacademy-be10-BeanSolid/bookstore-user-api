package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
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
            @ApiResponse(responseCode = "200", description = "포인트 내역 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class), examples = @ExampleObject(
                    name = "PointListExample",
                    summary = "포인트 내역 조회 예시",
                    value = """
                            {
                                               "content": [
                                                 {
                                                   "pointId": 363,
                                                   "userId": "test2",
                                                   "typeId": 29,
                                                   "orderId": null,
                                                   "earnedAndUsedAt": "2025-07-24 13:37:16",
                                                   "earnedAndUsedPoint": "500p 적립"
                                                 }
                                               ],
                                               "pageable": {
                                                 "pageNumber": 0,
                                                 "pageSize": 10,
                                                 "sort": [
                                                   {
                                                     "direction": "DESC",
                                                     "property": "pointId",
                                                     "ignoreCase": false,
                                                     "nullHandling": "NATIVE",
                                                     "ascending": false,
                                                     "descending": true
                                                   }
                                                 ],
                                                 "offset": 0,
                                                 "unpaged": false,
                                                 "paged": true
                                               },
                                               "last": false,
                                               "totalElements": 47,
                                               "totalPages": 5,
                                               "first": true,
                                               "size": 10,
                                               "number": 0,
                                               "sort": [
                                                 {
                                                   "direction": "DESC",
                                                   "property": "pointId",
                                                   "ignoreCase": false,
                                                   "nullHandling": "NATIVE",
                                                   "ascending": false,
                                                   "descending": true
                                                 }
                                               ],
                                               "numberOfElements": 10,
                                               "empty": false
                                             }
            """
            ))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(유효성 검증 실패 - X-USER-ID 헤더 미존재)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
                            {
                              "timestamp": "2025-07-24 13:39:27",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "path": "/users/me/point",
                              "message": "User ID must not be blank"
                            }
            """
            ))),
            @ApiResponse(responseCode = "404", description = "없는 유저ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "NotFoundExample",
                    summary = "존재하지 않는 유저 ID 예시",
                    value = """
                            {
                                              "timestamp": "2025-07-24 13:42:34",
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "path": "/users/me/point",
                                              "message": "User ID : gdfgfdhngf not found"
                            }
            """
            )))
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
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "ForbiddenExample",
                    summary = "유저 ID 불일치 예시",
                    value = """
                            {
                                              "timestamp": "2025-07-24 13:44:17",
                                              "status": 403,
                                              "error": "FORBIDDEN",
                                              "path": "/users/me/point",
                                              "message": "요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다."
                            }
            """
            ))),
            @ApiResponse(responseCode = "409", description = "옳지 않은 주문 번호", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "ConflictExample",
                    summary = "옳지 않은 주문 번호 예시-db 제약조건 위반",
                    value = """
                            {
                                              "timestamp": "2025-07-24 13:45:38",
                                              "status": 409,
                                              "error": "Conflict",
                                              "path": "/users/me/point",
                                              "message": "Cannot add or update a child row: a foreign key constraint fails (`project_be10_team3_dev`.`points`, CONSTRAINT `points_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`))"
                            }
            """
            )))
    })
    ResponseEntity<ResponsePoint> savePoint(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody PointCreateRequest pointCreateRequest,
            BindingResult bindingResult
    );
}