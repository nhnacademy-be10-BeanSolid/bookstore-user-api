package com.nhnacademy.bookstoreuserapi.controller.interfaces;

import com.nhnacademy.bookstoreuserapi.controller.advice.ErrorMessage;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저등급 API", description = "유저등급에 관한 Controller **등급 타입 = 1 : BASIC, 2 : ROYAL, 3 : GOLD, 4 : PLATINUM**")
public interface UserGradeControllerDoc {


    @Operation(summary = "유저 등급 추가", description = "유저 등급을 추가합니다. 필요 파라미터 : 등급명(등급타입에 있어야함), 승급에 필요한 금액")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "유저 등급 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserGrade.class))),
            @ApiResponse(responseCode = "409", description = "유저 등급이 이미 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                    {
                      "timeStamp": "2025-06-24T07:39:23.364457Z",
                      "status": 409,
                      "error": "Conflict",
                      "path": "/users/grade",
                      "message": "Grade : GOLD already exists."
                    }
                    """))),
    })
    @RequestBody(
            description = "유저 등급 생성 요청",
            required = true,
            content = @Content(schema = @Schema(implementation = UserGradeCreateRequest.class))
    )
    @PostMapping
    ResponseEntity<ResponseUserGrade> addUserGrade(@Valid @RequestBody UserGradeCreateRequest userGrade, BindingResult bindingResult);


    @Operation(summary = "유저 등급 수정", description = "유저 등급의 필요 금액을 수정합니다. 필요 파라미터 : 등급명, 수정할 승급에 필요한 금액")
    @RequestBody(
            description = "유저 등급 수정 요청",
            required = true,
            content = @Content(schema = @Schema(implementation = UserGradeUpdateRequest.class))
    )
    @Parameter(
            name = "gradeName",
            description = "수정할 유저 등급의 이름 (BASIC, ROYAL, GOLD, PLATINUM 중 하나)",
            required = true,
            schema = @Schema(type = "string", example = "GOLD")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 등급 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserGrade.class))),
            @ApiResponse(responseCode = "404", description = "유저 등급이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                    {
                      "timeStamp": "2025-06-24T07:39:23.364457Z",
                      "status": 404,
                      "error": "Not Found",
                      "path": "/users/grade/GOLD",
                      "message": "Grade : GOLD does not exist."
                    }
                    """))),
    })
    @PutMapping("/{gradeName}")
    ResponseEntity<ResponseUserGrade> updateUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName, @Valid @RequestBody UserGradeUpdateRequest userGrade, BindingResult bindingResult);


    @Operation(summary = "유저 등급 조회", description = "유저 등급을 조회합니다. 필요 파라미터 : 등급명")
    @Parameters({
            @Parameter(name = "gradeName", description = "조회할 유저 등급의 이름 (BASIC, ROYAL, GOLD, PLATINUM 중 하나)", required = true, schema = @Schema(type = "string", example = "GOLD"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 등급 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserGrade.class))),
            @ApiResponse(responseCode = "404", description = "유저 등급이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                    {
                      "timeStamp": "2025-06-24T07:39:23.364457Z",
                      "status": 404,
                      "error": "Not Found",
                      "path": "/users/grade/GOLD",
                      "message": "Grade : GOLD does not exist."
                    }
                    """))),
    })
    @GetMapping("/{gradeName}")
    ResponseEntity<ResponseUserGrade> getUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName);

    @Operation(summary = "유저 등급 삭제", description = "유저 등급을 삭제합니다. 필요 파라미터 : 등급명")
    @Parameters({
            @Parameter(name = "gradeName", description = "삭제할 유저 등급의 이름 (BASIC, ROYAL, GOLD, PLATINUM 중 하나)", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "유저 등급 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "유저 등급이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                    examples = @ExampleObject(value = """
                    {
                      "timeStamp": "2025-06-24T07:39:23.364457Z",
                      "status": 404,
                      "error": "Not Found",
                      "path": "/users/grade/GOLD",
                      "message": "Grade : GOLD does not exist."
                    }
                    """))),
    })
    @DeleteMapping("/{gradeName}")
    ResponseEntity<Void> deleteUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName);

    @Operation(summary = "모든 유저 등급 조회", description = "모든 유저 등급을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 유저 등급 조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseUserGrade.class)))),
    })
    @GetMapping("/all")
    ResponseEntity<List<ResponseUserGrade>> getAllUserGrades();
}
