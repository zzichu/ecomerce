package com.ecommerce.domain.review.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewId;
    private Integer reviewScore;
    private Long purchaseId;
    private Long itemId;
    private String comment;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modificatedDate;
    private int deletedStatus;
	public Long getReviewId() {
		return reviewId;
	}
	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	public Integer getReviewScore() {
		return reviewScore;
	}
	public void setReviewScore(Integer reviewScore) {
		this.reviewScore = reviewScore;
	}
	public Long getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreationUser() {
		return creationUser;
	}
	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getModificatedDate() {
		return modificatedDate;
	}
	public void setModificatedDate(LocalDateTime modificatedDate) {
		this.modificatedDate = modificatedDate;
	}
	public int getDeletedStatus() {
		return deletedStatus;
	}
	public void setDeletedStatus(int deletedStatus) {
		this.deletedStatus = deletedStatus;
	}

    
}
