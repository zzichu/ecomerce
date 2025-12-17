package com.ecommerce.domain.coupon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.coupon.entity.CouponEntity;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    List<CouponEntity> findAllByDeletedStatusFalse();
    Optional<CouponEntity> findByUserIdAndCouponIdAndDeletedStatus(Long couponId, Long userId, int deletedStatus);
}
