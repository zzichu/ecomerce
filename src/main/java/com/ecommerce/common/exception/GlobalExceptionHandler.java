package com.ecommerce.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.common.response.ApiResponse;

// @RestControllerAdvice //TODO : AOP 개념과 controllerAdvice 어노테이션 찾아보기
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 반환
    }

    //TODO: bad request 추가
    //Service layer exception 처리
    //repository layer도!
}
