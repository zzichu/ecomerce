package com.ecommerce.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.domain.auth.dto.LoginRequest;
import com.ecommerce.domain.auth.dto.TokenDto;
import com.ecommerce.domain.auth.dto.UserDto;
import com.ecommerce.domain.auth.service.AuthService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<UserDto>> join(@RequestBody UserDto userDto) {
        authService.registerUser(userDto);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "로그인 성공", userDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("userId") Long userId) {
        UserDto user = authService.getUserById(userId);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "유저 조회 성공", user);
        return ResponseEntity.ok(response);
    }
        
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        TokenDto tokenDto = new TokenDto(jwt);
        return new ResponseEntity<>(tokenDto, headers, HttpStatus.OK);
    }

    
    @PatchMapping("/user")
    public ResponseEntity<ApiResponse<UserDto>> withDraw(@RequestParam Long userId) {
        authService.withDraw(userId);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "회원 탈퇴 성공", null);
        return ResponseEntity.ok(response);
    }
    
}

