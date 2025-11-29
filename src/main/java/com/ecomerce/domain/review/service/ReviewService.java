package com.ecomerce.domain.review.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.ecomerce.domain.review.dto.ReviewDto;
import com.ecomerce.domain.review.entity.ReviewEntity;
import com.ecomerce.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void createReview(ReviewDto reviewDto) {
        reviewRepository.save(ReviewEntity.builder()
            .reviewId(reviewDto.getReviewId())
            .reviewScore(reviewDto.getReviewScore())
            .purchaseId(reviewDto.getPurchaseId())
            .itemId(reviewDto.getItemId())
            .comment(reviewDto.getComment())
            .creationUser(reviewDto.getCreationUser())
            .deletedStatus(reviewDto.getDeletedStatus())
            .build()
        );
    }

    @Transactional
    public ReviewDto updateReview(ReviewDto reviewDto) {
        ReviewEntity entity = reviewRepository.findById(reviewDto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
        entity.updateReview(reviewDto);
        reviewRepository.save(entity);

        return ReviewDto.builder()
            .reviewId(entity.getReviewId())
            .reviewScore(entity.getReviewScore())
            .purchaseId(entity.getPurchaseId())
            .itemId(entity.getItemId())
            .comment(entity.getComment())
            .creationUser(entity.getCreationUser())
            .createdDate(entity.getCreatedDate())
            .modificatedDate(entity.getModificatedDate())
            .deletedStatus(entity.getDeletedStatus())
            .build();
    }


    @Transactional
    public ReviewDto deleteReview(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
        review.delete(); // 1=삭제됨 // TODO: set바꾸기..(수정완)
        
        reviewRepository.save(review);

        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewScore(review.getReviewScore())
                .purchaseId(review.getPurchaseId())
                .itemId(review.getItemId())
                .comment(review.getComment())
                .creationUser(review.getCreationUser())
                .createdDate(review.getCreatedDate())
                .modificatedDate(review.getModificatedDate())
                .deletedStatus(review.getDeletedStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByItemId(Long itemId) {
        List<ReviewEntity> entityList = reviewRepository.findByItemIdAndDeletedStatus(itemId, 0); // TODO: jpa쓰는 이유 찾아보기.
        
        return entityList.stream() // TODO : stream 왜 쓰는지 찾아보기
                .map(entity -> ReviewDto.builder()
                        .reviewId(entity.getReviewId())
                        .reviewScore(entity.getReviewScore())
                        .purchaseId(entity.getPurchaseId())
                        .itemId(entity.getItemId())
                        .comment(entity.getComment())
                        .creationUser(entity.getCreationUser())
                        .createdDate(entity.getCreatedDate())
                        .modificatedDate(entity.getModificatedDate())
                        .deletedStatus(entity.getDeletedStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long reviewId) {
        ReviewEntity entity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
        return ReviewDto.builder()
                .reviewId(entity.getReviewId())
                .reviewScore(entity.getReviewScore())
                .purchaseId(entity.getPurchaseId())
                .itemId(entity.getItemId())
                .comment(entity.getComment())
                .creationUser(entity.getCreationUser())
                .createdDate(entity.getCreatedDate())
                .modificatedDate(entity.getModificatedDate())
                .deletedStatus(entity.getDeletedStatus())
                .build();
    }


}
