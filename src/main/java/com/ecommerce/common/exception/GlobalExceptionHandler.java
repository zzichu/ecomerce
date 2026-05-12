package com.ecommerce.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.common.response.ApiResponse;

import org.springframework.dao.DataAccessException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // [Bad Request] 잘못된 인자 전달 시 발생 (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // [Bad Request] @Valid 유효성 검사 실패 시 발생 (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiResponse<?> response = new ApiResponse<>(false, errorMessage, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // [Unauthorized] 토큰 없음 또는 유효하지 않은 토큰 (401)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, "토큰이 없거나 유효하지 않습니다.", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // [Forbidden] 권한 없음 (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, "접근 권한이 없습니다.", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // [Repository Layer] 데이터를 찾을 수 없는 경우 (404)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, "요청하신 자원을 찾을 수 없습니다.", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // [Repository Layer] DB 관련 예외 (500)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleDatabaseException(DataAccessException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, "데이터베이스 처리 중 오류가 발생했습니다.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // [Global] 위에서 정의되지 않은 모든 서버 내부 예외 처리 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAllException(Exception ex) {
        ex.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(false, "서버 내부 오류가 발생했습니다.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}