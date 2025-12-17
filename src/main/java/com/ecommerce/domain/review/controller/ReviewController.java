package com.ecommerce.domain.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.domain.review.dto.ReviewDto;
import com.ecommerce.domain.review.service.ReviewService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    // 리뷰 작성
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(@RequestBody ReviewDto reviewDto) {
        reviewService.createReview(reviewDto);
        ApiResponse<ReviewDto> response = new ApiResponse<>(true, "리뷰 작성 성공", reviewDto);
        return ResponseEntity.ok(response);
    }

    // 리뷰 수정
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto updated = reviewService.updateReview(reviewDto);
        ApiResponse<ReviewDto> response = new ApiResponse<>(true, "리뷰 수정 성공", updated);
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        ReviewDto deletedReview = reviewService.deleteReview(reviewId);
        ApiResponse<ReviewDto> response = new ApiResponse<>(true, "리뷰 삭제 성공", deletedReview);
        return ResponseEntity.ok(response);
    }   

    // 특정 상품에 대한 리뷰 전체 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByItem(@PathVariable("itemId")  Long itemId) {
        List<ReviewDto> reviews = reviewService.getReviewsByItemId(itemId);
        ApiResponse<List<ReviewDto>> response = new ApiResponse<>(true, "상품 리뷰 전체 조회 성공", reviews);
        return ResponseEntity.ok(response);
    }

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable("reviewId") Long reviewId) {
        ReviewDto review = reviewService.getReviewById(reviewId);
        ApiResponse<ReviewDto> response = new ApiResponse<>(true, "리뷰 조회 성공", review);
        return ResponseEntity.ok(response);
    }

}

