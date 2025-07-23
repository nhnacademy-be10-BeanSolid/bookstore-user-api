package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.review.domain.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.review.domain.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReviewByUser;
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
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "리뷰 API", description = "리뷰에 관한 Controller 입니다.")
public interface ReviewControllerDoc {

    @Operation(summary = "리뷰 추가", description = "새로운 리뷰를 추가합니다.")
    @RequestBody(
            description = "리뷰 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = ReviewCreateRequest.class),
                    examples = @ExampleObject(
                            name = "ReviewCreateRequestExample",
                            summary = "리뷰 생성 요청 예시",
                            value = """
                                    {
                                      "evaluationScore": 5,
                                      "reviewContent": "재밌어요",
                                      "imageUrls": [
                                        "http://example.com/image1.jpg"
                                      ],
                                      "userId": "test2",
                                      "bookId": 1
                                    }
                                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseReview.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
                            {
                              "timestamp": "2025-07-23 15:31:06",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "path": "/reviews/me",
                              "message": "User ID must not be blank"
                            }
            """
            ))),
            @ApiResponse(responseCode = "403", description = "잘못된 요청(도서를 구매하지 않음, 권한이 없음)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = {@ExampleObject(
                            name = "PurchaseRequired",
                            summary = "도서를 구매하지 않은 경우",
                            value = """
                                    {
                                      "timestamp": "2025-07-23 14:56:50",
                                      "status": 403,
                                      "error": "FORBIDDEN",
                                      "path": "/reviews/me",
                                      "message": "해당 사용자는 이 책을 구매하지 않았습니다."
                                    }
                                    """
                    ),
                    @ExampleObject(
                            name = "ForbiddenExample",
                            summary = "유저 ID 불일치 예시",
                            value = """
                                    {
                                      "timestamp": "2025-07-23 15:06:32",
                                      "status": 403,
                                      "error": "FORBIDDEN",
                                      "path": "/reviews/me",
                                      "message": "요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다."
                                    }
                                    """
                    )
            })),
            @ApiResponse(responseCode = "409", description = "이미 리뷰가 작성된 도서임", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(
                            name = "ReviewAlreadyExists",
                            summary = "이미 리뷰가 작성된 도서인 경우",
                            value = """
                                    {
                                      "timestamp": "2025-07-23 14:58:41",
                                      "status": 409,
                                      "error": "CONFLICT",
                                      "path": "/reviews/me",
                                      "message": "Review already exists for user: test2 at book: 1"
                                    }
                                    """
                    )))
    })
    ResponseEntity<ResponseReview> addReview(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @RequestBody ReviewCreateRequest review,
            BindingResult bindingResult
    );

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
    @Parameter(name = "reviewId", description = "수정할 리뷰 ID", required = true, example = "1")
    @RequestBody(
            description = "리뷰 수정 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = ReviewUpdateRequest.class), examples = {@ExampleObject(
                    name = "ReviewUpdateRequestExample",
                    summary = "리뷰 수정 요청 예시",
                    value = """
                            {
                              "evaluationScore": 5,
                              "reviewContent": "리뷰내용수정",
                              "imageUrls": []
                            }
                            """
            )})
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseReview.class),
            examples = @ExampleObject(
                            name = "ReviewUpdateResponse",
                            summary = "리뷰 수정 성공 예시",
                            value = """
                                    {
                                      "reviewId": 5,
                                      "evaluationScore": 5,
                                      "reviewContent": "리뷰내용수정",
                                      "reviewImages": [],
                                      "reviewedAt": "2025-07-23 10:50:16",
                                      "updatedAt": "2025-07-23 15:48:46",
                                      "userId": "test2",
                                      "bookId": 1
                                    }
                                    """
                    ))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "BadRequestExample",
                    summary = "X-USER-ID 헤더 미존재 예시",
                    value = """
                            {
                              "timestamp": "2025-07-23 15:31:06",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "path": "/reviews/me/5",
                              "message": "User ID must not be blank"
                            }
            """
            ))),
            @ApiResponse(responseCode = "403", description = "잘못된 요청(도서를 구매하지 않음, 권한이 없음)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples =
                            @ExampleObject(
                                    name = "ForbiddenExample",
                                    summary = "유저 ID 불일치 예시",
                                    value = """
                                            {
                                              "timestamp": "2025-07-23 15:46:30",
                                              "status": 403,
                                              "error": "FORBIDDEN",
                                              "path": "/reviews/me/5",
                                              "message": "요청한 유저 ID와 리소스의 유저 ID가 일치하지 않습니다."
                                            }
                                    """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(
                            name = "ReviewNotFound",
                            summary = "리뷰를 찾을 수 없음",
                            value = """
                                    {
                                      "timestamp": "2025-07-23 15:47:48",
                                      "status": 404,
                                      "error": "NOT_FOUND",
                                      "path": "/reviews/me/54",
                                      "message": "Review ID: 54 not found"
                                    }
                                    """
            ))),
    })
    ResponseEntity<ResponseReview> updateReview(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @PathVariable @Min(1) long reviewId,
            @Valid @RequestBody ReviewUpdateRequest review,
            BindingResult bindingResult
    );

    @Operation(summary = "단일 리뷰 조회", description = "지정된 ID의 단일 리뷰 정보를 조회합니다.")
    @Parameter(name = "reviewId", description = "조회할 리뷰 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseReview.class), examples = @ExampleObject(
                    name = "ReviewResponse",
                    summary = "리뷰 조회 성공 예시",
                    value = """
                            {
                                  "reviewId": 5,
                                  "evaluationScore": 3,
                                  "reviewContent": "평점",
                                  "reviewImages": [],
                                  "reviewedAt": "2025-07-23 10:50:16",
                                  "updatedAt": "2025-07-23 13:04:35",
                                  "userId": "test2",
                                  "bookId": 1
                            }
                            """
            ))),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class), examples = @ExampleObject(
                    name = "ReviewNotFound",
                    summary = "리뷰를 찾을 수 없음",
                    value = """
                            {
                              "timestamp": "2025-07-23 13:56:00",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/reviews/1",
                              "message": "Review ID: 1 not found"
                            }
                            """
            )))
    })
    ResponseEntity<ResponseReview> getReview(
            @PathVariable @Min(1) long reviewId
    );

    @Operation(summary = "사용자 ID로 리뷰 조회", description = "지정된 사용자 ID로 리뷰 목록을 페이지네이션하여 조회합니다.")
    @Parameter(name = "userId", description = "조회할 사용자 ID", required = true, example = "test2")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "sort", description = "정렬 기준 (예: reviewId,desc)", example = "reviewId,desc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class), examples = @ExampleObject(
                    name = "UserReviewsResponse",
                    summary = "사용자 리뷰 조회 성공 예시",
                    value = """
                            {
                               "content": [
                                 {
                                   "reviewId": 5,
                                   "evaluationScore": 3,
                                   "reviewContent": "평점",
                                   "reviewedAt": "2025-07-23 10:50:16",
                                   "updatedAt": "2025-07-23 13:04:35",
                                   "userId": "test2",
                                   "bookId": 1
                                 }
                               ],
                               "pageable": {
                                 "pageNumber": 0,
                                 "pageSize": 10,
                                 "sort": [
                                   {
                                     "direction": "DESC",
                                     "property": "reviewId",
                                     "ignoreCase": false,
                                     "nullHandling": "NATIVE",
                                     "descending": true,
                                     "ascending": false
                                   }
                                 ],
                                 "offset": 0,
                                 "paged": true,
                                 "unpaged": false
                               },
                               "last": true,
                               "totalPages": 1,
                               "totalElements": 1,
                               "first": true,
                               "size": 10,
                               "number": 0,
                               "sort": [
                                 {
                                   "direction": "DESC",
                                   "property": "reviewId",
                                   "ignoreCase": false,
                                   "nullHandling": "NATIVE",
                                   "descending": true,
                                   "ascending": false
                                 }
                               ],
                               "numberOfElements": 1,
                               "empty": false
                             }
                            """
            )))
    })
    ResponseEntity<Page<ResponseSimpleReviewByUser>> getReviewByUserId(
            @PathVariable @NotBlank @Size(max = 20) String userId,
            Pageable pageable
    );

    @Operation(summary = "도서 ID로 리뷰 조회", description = "지정된 도서 ID로 리뷰 목록을 페이지네이션하여 조회합니다.")
    @Parameter(name = "bookId", description = "조회할 도서 ID", required = true, example = "1")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "sort", description = "정렬 기준 (예: reviewId,desc)", example = "reviewId,desc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class), examples = @ExampleObject(
                    name = "BookReviewsResponse",
                    summary = "도서 리뷰 조회 성공 예시",
                    value = """
                            {
                                "content": [
                                  {
                                    "reviewId": 5,
                                    "evaluationScore": 3,
                                    "reviewContent": "평점",
                                    "reviewedAt": "2025-07-23 10:50:16",
                                    "updatedAt": "2025-07-23 13:04:35",
                                    "userId": "test2",
                                    "bookId": 1
                                  }
                                ],
                                "pageable": {
                                  "pageNumber": 0,
                                  "pageSize": 10,
                                  "sort": [
                                    {
                                      "direction": "DESC",
                                      "property": "reviewId",
                                      "ignoreCase": false,
                                      "nullHandling": "NATIVE",
                                      "ascending": false,
                                      "descending": true
                                    }
                                  ],
                                  "offset": 0,
                                  "paged": true,
                                  "unpaged": false
                                },
                                "last": true,
                                "totalPages": 1,
                                "totalElements": 1,
                                "first": true,
                                "size": 10,
                                "number": 0,
                                "sort": [
                                  {
                                    "direction": "DESC",
                                    "property": "reviewId",
                                    "ignoreCase": false,
                                    "nullHandling": "NATIVE",
                                    "ascending": false,
                                    "descending": true
                                  }
                                ],
                                "numberOfElements": 1,
                                "empty": false
                              }
                            """
            ))),
    })
    ResponseEntity<Page<ResponseSimpleReview>> getReviewByBookId(
            @PathVariable @Min(1) long bookId,
            Pageable pageable
    );

    @Operation(summary = "도서 ID로 리뷰 개수 조회", description = "지정된 도서 ID에 대한 리뷰 개수를 조회합니다.")
    @Parameter(name = "bookId", description = "리뷰 개수를 조회할 도서 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 개수 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
    })
    ResponseEntity<Long> countReviewsByBookId(
            @PathVariable @Min(1) long bookId
    );

    @Operation(summary = "도서 ID로 평균 평점 조회", description = "지정된 도서 ID에 대한 평균 평점을 조회합니다.")
    @Parameter(name = "bookId", description = "평균 평점을 조회할 도서 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "평균 평점 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Double.class))),
    })
    ResponseEntity<Double> getAverageEvaluationScoreByBookId(
            @PathVariable @Min(1) long bookId
    );
}