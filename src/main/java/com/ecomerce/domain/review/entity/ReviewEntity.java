package com.ecomerce.domain.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.ecomerce.domain.review.dto.ReviewDto;


@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(name = "review_score")
    private Integer reviewScore;

    @Column(name = "purchase_id", nullable = false)
    private Long purchaseId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(columnDefinition = "LONGTEXT")
    private String comment;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "created_date", updatable = false)
    @CreatedBy
    @CreatedDate
    private LocalDateTime createdDate; //TODO: common에서 상속받아서..
    
    @Column(name = "modificated_date")
    @LastModifiedBy
    @LastModifiedDate
    private LocalDateTime modificatedDate;

    @Column(name = "deleted_status")
    private int deletedStatus;

    public void updateReview(ReviewDto reviewDto) {
        this.reviewScore = reviewDto.getReviewScore();
        this.comment = reviewDto.getComment();
        this.deletedStatus = reviewDto.getDeletedStatus();
    }

    public void delete() {
        this.deletedStatus = 1;
    }
}
