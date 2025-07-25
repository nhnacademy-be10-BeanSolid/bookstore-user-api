package com.nhnacademy.bookstoreuserapi.common.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.cart.context.CartContext;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartAddItemRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateItemsRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.request.CartUpdateRequest;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartCreateResponse;
import com.nhnacademy.bookstoreuserapi.cart.dto.response.CartResponse;
import com.nhnacademy.bookstoreuserapi.common.controller.advice.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "장바구니 API", description = "장바구니에 관한 Controller 입니다.")
@SecurityRequirement(name = "X-OWNER-TYPE")
@SecurityRequirement(name = "X-USER-ID")
public interface CartControllerDoc {

    @Operation(summary = "장바구니 생성", description = "새로운 장바구니를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "장바구니 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartCreateResponse.class),
            examples = @ExampleObject(value = """
                    {
                      "cartId": 8,
                      "userId": "test",
                      "guestUUID": null,
                      "ownerType": "USER",
                      "createdAt": "2025-07-24 16:01:49"
                    }
                    """
            ))),
            @ApiResponse(responseCode = "400", description = "헤더 미존재", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-07-24 16:02:41",
                              "status": 400,
                              "error": "Bad Request",
                              "path": "/carts/me",
                              "message": "Required request header 'X-OWNER-TYPE' for method parameter type OwnerType is not present"
                            }
                    """))),
            @ApiResponse(responseCode = "404", description = "유저 ID를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                            {
                                      "timestamp": "2025-07-24 16:03:37",
                                      "status": 404,
                                      "error": "NOT_FOUND",
                                      "path": "/carts/me",
                                      "message": "User ID : fasdkljf not found"
                            }
                    """)))
    })
    ResponseEntity<CartCreateResponse> createCart(
            @Parameter(hidden = true) CartContext ctx
    );

    @Operation(summary = "장바구니 조회", description = "현재 사용자의 장바구니 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "400", description = "헤더 미존재", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-07-24 15:56:59",
                      "status": 400,
                      "error": "Bad Request",
                      "path": "/carts/me",
                      "message": "Required request header 'X-OWNER-TYPE' for method parameter type OwnerType is not present"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),examples = @ExampleObject(
                    value = """
                            {
                              "timestamp": "2025-07-24 15:59:44",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/carts/me",
                              "message": "해당 사용자(userId: test)의 장바구니를 찾을 수 없습니다."
                            }
                    """
            )))
    })
    ResponseEntity<CartResponse> getCart(
            @Parameter(hidden = true) CartContext ctx
    );

    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 새로운 상품을 추가합니다.")
    @RequestBody(
            description = "장바구니에 추가할 상품 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = CartAddItemRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
    })
    ResponseEntity<CartResponse> addItemToCart(
            @Parameter(hidden = true) CartContext ctx,
            @Valid @RequestBody CartAddItemRequest request,
            BindingResult bindingResult
    );

    @Operation(summary = "장바구니 상품 수량 업데이트", description = "장바구니에 있는 특정 상품의 수량을 업데이트합니다.")
    @Parameter(name = "itemId", description = "업데이트할 상품 ID", required = true, example = "1")
    @RequestBody(
            description = "업데이트할 수량 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = CartUpdateRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수량 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-07-24 16:06:24",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/carts/me/items/144",
                              "message": "장바구니 아이템(ID: 144)을 찾을 수 없습니다."
                            }
                    """)))
    })
    ResponseEntity<CartResponse> updateItemQuantity(
            @Parameter(hidden = true) CartContext ctx,
            @PathVariable("itemId") Long itemId,
            @Valid @org.springframework.web.bind.annotation.RequestBody CartUpdateRequest request,
            BindingResult bindingResult
    );

    @Operation(summary = "장바구니에서 상품 삭제", description = "장바구니에서 특정 상품을 삭제합니다.")
    @Parameter(name = "itemId", description = "삭제할 상품 ID", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없음(USERID이상)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                            {
                                      "timestamp": "2025-07-24 16:06:24",
                                      "status": 404,
                                      "error": "NOT_FOUND",
                                      "path": "/carts/me/items/144",
                                      "message": "해당 사용자(userId: fadsfasdf)의 장바구니를 찾을 수 없습니다."
                                    }
                    """)))
    })
    ResponseEntity<CartResponse> deleteItemFromCart(
            @Parameter(hidden = true) CartContext ctx,
            @PathVariable("itemId") Long itemId
    );

    @Operation(summary = "장바구니에서 여러 상품 삭제", description = "장바구니에서 여러 상품을 한 번에 삭제합니다.")
    @RequestBody(
            description = "삭제할 상품 ID 목록",
            required = true,
            content = @Content(schema = @Schema(implementation = List.class), examples = @ExampleObject(
                    value = """
                            [1, 2, 3]
                    """
            ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품들 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class)))
    })
    ResponseEntity<CartResponse> deleteItemsFromCart(
            @Parameter(hidden = true) CartContext ctx,
            @org.springframework.web.bind.annotation.RequestBody List<Long> itemIds
    );

    @Operation(summary = "장바구니 상품들 일괄 업데이트", description = "장바구니에 있는 여러 상품의 정보를 일괄적으로 업데이트합니다.")
    @RequestBody(
            description = "업데이트할 상품 정보 목록",
            required = true,
            content = @Content(schema = @Schema(implementation = CartUpdateItemsRequest.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품들 일괄 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
            examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-07-24 16:10:41",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "path": "/carts/me/items",
                              "message": "장바구니 아이템(ID: 2)을 찾을 수 없습니다."
                            }
                    """)))
    })
    ResponseEntity<CartResponse> updateItemsInCart(
            @Parameter(hidden = true) CartContext ctx,
            @Valid @org.springframework.web.bind.annotation.RequestBody CartUpdateItemsRequest request,
            BindingResult bindingResult
    );
}