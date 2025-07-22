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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "리뷰 API", description = "리뷰에 관한 Controller 입니다.")
public interface ReviewControllerDoc {

    @Operation(summary = "리뷰 추가", description = "새로운 리뷰를 추가합니다.")
    @RequestBody(
            description = "리뷰 생성 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = ReviewCreateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseReview.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseReview> addReview(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody ReviewCreateRequest review,
            BindingResult bindingResult
    );

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
    @Parameter(name = "reviewId", description = "수정할 리뷰 ID", required = true, example = "1")
    @RequestBody(
            description = "리뷰 수정 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = ReviewUpdateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseReview.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseReview> updateReview(
            @Parameter(hidden = true) @AuthenticatedUserId String userId,
            @PathVariable @Min(1) long reviewId,
            @Valid @org.springframework.web.bind.annotation.RequestBody ReviewUpdateRequest review,
            BindingResult bindingResult
    );

    @Operation(summary = "단일 리뷰 조회", description = "지정된 ID의 단일 리뷰 정보를 조회합니다.")
    @Parameter(name = "reviewId", description = "조회할 리뷰 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseReview.class))),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<ResponseReview> getReview(
            @PathVariable @Min(1) long reviewId
    );

    @Operation(summary = "사용자 ID로 리뷰 조회", description = "지정된 사용자 ID로 리��� 목록을 페이지네이션하여 조회합니다.")
    @Parameter(name = "userId", description = "조회할 사용자 ID", required = true, example = "testuser")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "sort", description = "정렬 기준 (예: reviewId,desc)", example = "reviewId,desc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
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
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "도서를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Page<ResponseSimpleReview>> getReviewByBookId(
            @PathVariable @Min(1) long bookId,
            Pageable pageable
    );

    @Operation(summary = "도서 ID로 리뷰 개수 조회", description = "지정된 도서 ID에 대한 리뷰 개수를 조회합니다.")
    @Parameter(name = "bookId", description = "리뷰 개수를 조회할 도서 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 개수 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "도서를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Long> countReviewsByBookId(
            @PathVariable @Min(1) long bookId
    );

    @Operation(summary = "도서 ID로 평균 평점 조회", description = "지정된 도서 ID에 대한 평균 평점을 조회합니다.")
    @Parameter(name = "bookId", description = "평균 평점을 조회할 도서 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "평균 평점 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "도서를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    ResponseEntity<Double> getAverageEvaluationScoreByBookId(
            @PathVariable @Min(1) long bookId
    );
}