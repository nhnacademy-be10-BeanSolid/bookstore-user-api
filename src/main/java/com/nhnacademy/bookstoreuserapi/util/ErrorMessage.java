package com.nhnacademy.bookstoreuserapi.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorMessage {
    String message;
    HttpStatus httpStatus;
}
