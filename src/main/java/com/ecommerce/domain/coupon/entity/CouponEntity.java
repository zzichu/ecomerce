package com.ecommerce.domain.coupon.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.ecommerce.domain.coupon.dto.CouponDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "coupon_name", nullable = false, length = 255)
    private String couponName;

    @Column(name = "discount_rate")
    private int discountRate;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "ended_date")
    private LocalDateTime endedDate;

    @Column(name = "creation_user")
    private Long creationUser;

    @Column(name = "created_date")
    @CreatedBy
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedBy
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "deleted_status")
    private int deletedStatus = 0;

    public void update(CouponDto couponDto) {
        this.couponName = couponDto.getCouponName();
        this.discountRate = couponDto.getDiscountRate();
        this.usedDate = couponDto.getUsedDate();
        this.userId = couponDto.getUserId();
        this.startedDate = couponDto.getStartedDate();
        this.endedDate = couponDto.getEndedDate();
        this.creationUser = couponDto.getCreationUser();
    }

    public void delete() {
        this.deletedStatus = 1;
    }
}
