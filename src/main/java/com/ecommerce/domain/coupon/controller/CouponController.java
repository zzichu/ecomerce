package com.ecommerce.domain.coupon.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.domain.coupon.dto.CouponDto;
import com.ecommerce.domain.coupon.service.CouponService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
	
	@Autowired
    private CouponService couponService;


    // 쿠폰 등록
    @PostMapping
    public ResponseEntity<ApiResponse<CouponDto>> createCoupon(@RequestBody CouponDto couponDto) {
        couponService.createCoupon(couponDto);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "쿠폰 등록 성공", couponDto);
        return ResponseEntity.ok(response);
    
    }

    // 쿠폰 수정
    @PutMapping //TODO: PATCH와 PUT 차이 찾아보기
    //TODO: ResponseEntity 찾아보기.. (한번 더!)
    public ResponseEntity<ApiResponse<CouponDto>> updateCoupon(@RequestBody CouponDto couponDto) {
        CouponDto updated = couponService.updateCoupon(couponDto);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "쿠폰 수정 성공", updated);
        return ResponseEntity.ok(response);
    }

    // 쿠폰 삭제
    @PatchMapping("/{couponId}")
    public ResponseEntity<ApiResponse<CouponDto>> deleteCoupon(@PathVariable Long couponId) {
        CouponDto deleted = couponService.deleteCoupon(couponId);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "쿠폰 삭제 성공", deleted);
        return ResponseEntity.ok(response);
    }
    
    // 쿠폰 조회
    @GetMapping("/{userId}/{couponId}")
    public ResponseEntity<ApiResponse<CouponDto>> getCoupon(@PathVariable Long userId, @PathVariable Long couponId) {
        CouponDto coupon = couponService.getCouponByUserIdAndCouponId(userId, couponId);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "쿠폰 조회 성공", coupon);
        return ResponseEntity.ok(response);
    }

    // 쿠폰 사용 처리
    @PostMapping("/{userId}/{couponId}")
    public ResponseEntity<ApiResponse<CouponDto>> useCoupon(@PathVariable Long userId, @PathVariable Long couponId) {
        CouponDto updated = couponService.useCoupon(userId, couponId);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "쿠폰 사용 완료", updated);
        return ResponseEntity.ok(response);
    }
}


