package com.ecommerce.domain.coupon.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class CouponDto {
    private Long couponId;
    private String couponName;
    private int discountRate;
    private LocalDateTime usedDate;
    private Long userId;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;    
    private Long creationUser;
    private int deletedStatus;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public int getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(int discountRate) {
		this.discountRate = discountRate;
	}
	public LocalDateTime getUsedDate() {
		return usedDate;
	}
	public void setUsedDate(LocalDateTime usedDate) {
		this.usedDate = usedDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public LocalDateTime getStartedDate() {
		return startedDate;
	}
	public void setStartedDate(LocalDateTime startedDate) {
		this.startedDate = startedDate;
	}
	public LocalDateTime getEndedDate() {
		return endedDate;
	}
	public void setEndedDate(LocalDateTime endedDate) {
		this.endedDate = endedDate;
	}
	public Long getCreationUser() {
		return creationUser;
	}
	public void setCreationUser(Long creationUser) {
		this.creationUser = creationUser;
	}
	public int getDeletedStatus() {
		return deletedStatus;
	}
	public void setDeletedStatus(int deletedStatus) {
		this.deletedStatus = deletedStatus;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	

}