package com.ecommerce.domain.coupon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.domain.coupon.dto.CouponDto;
import com.ecommerce.domain.coupon.entity.CouponEntity;
import com.ecommerce.domain.coupon.repository.CouponRepository;

@Service
public class CouponService {

    //TODO: 생성자 주입 방식 3가지 차이점(한번 더!)
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    // 쿠폰 등록
    @Transactional // TODO: 트랜잭션의 성질 4가지(지속성, 원자성, 희소성, 일관성 영어로)
    public void createCoupon(CouponDto couponDto) {
    	CouponEntity couponEntity = CouponEntity.builder()
    	        .couponName(couponDto.getCouponName()) //TODO: JPA 변경 감지 공부하는 것..(한번 더!) 김영한 강의 jpa
    	        .discountRate(couponDto.getDiscountRate())
    	        .usedDate(couponDto.getUsedDate())
    	        .userId(couponDto.getUserId())
    	        .startedDate(couponDto.getStartedDate())
    	        .endedDate(couponDto.getEndedDate())
    	        .creationUser(couponDto.getCreationUser())
    	        .deletedStatus(couponDto.getDeletedStatus() != 1 ? couponDto.getDeletedStatus() : 0)
    	        .build();

    	couponRepository.save(couponEntity);
    }

    // 쿠폰 수정
    @Transactional
    public CouponDto updateCoupon(CouponDto couponDto) {
        Long couponId = couponDto.getCouponId();
        if (couponId == null) {
            throw new IllegalArgumentException("쿠폰 ID가 없습니다.");
        }

        CouponEntity coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
       
        //TODO: set 안쓰는 이유 (수정완)
        //setter를 사용하면 외부에서 상태변경하기 쉬움.
        coupon.update(couponDto);
        couponRepository.save(coupon);

        return CouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponName(coupon.getCouponName())
                .discountRate(coupon.getDiscountRate())
                .usedDate(coupon.getUsedDate())
                .userId(coupon.getUserId())
                .startedDate(coupon.getStartedDate())
                .endedDate(coupon.getEndedDate())
                .creationUser(coupon.getCreationUser())
                .deletedStatus(coupon.getDeletedStatus())
                .build();
    }

    // 쿠폰 삭제
    @Transactional
    public CouponDto deleteCoupon(Long couponId) {
        CouponEntity entity = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        entity.delete(); //TODO: 메소드 바꾸기 (수정완) (다음 시간 설명까지) 
        
        couponRepository.save(entity);

        return CouponDto.builder()
                .couponId(entity.getCouponId())
                .couponName(entity.getCouponName())
                .discountRate(entity.getDiscountRate())
                .usedDate(entity.getUsedDate())
                .userId(entity.getUserId())
                .startedDate(entity.getStartedDate())
                .endedDate(entity.getEndedDate())
                .creationUser(entity.getCreationUser())
                .deletedStatus(entity.getDeletedStatus())
                .build();
    }
    
    // 쿠폰 조회
    public CouponDto getCouponByUserIdAndCouponId(Long userId, Long couponId) {
        CouponEntity entity = couponRepository.findByUserIdAndCouponIdAndDeletedStatus(couponId, userId, 0)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        return CouponDto.builder()
                .couponId(entity.getCouponId())
                .couponName(entity.getCouponName())
                .discountRate(entity.getDiscountRate())
                .usedDate(entity.getUsedDate())
                .userId(entity.getUserId())
                .startedDate(entity.getStartedDate())
                .endedDate(entity.getEndedDate())
                .creationUser(entity.getCreationUser())
                .deletedStatus(entity.getDeletedStatus())
                .build();
    }

    // 쿠폰 사용 처리
    @Transactional
    public CouponDto useCoupon(Long userId, Long couponId) {
        CouponEntity entity = couponRepository.findByUserIdAndCouponIdAndDeletedStatus(couponId, userId, 0)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        couponRepository.save(entity);

        return CouponDto.builder()
                .couponId(entity.getCouponId())
                .couponName(entity.getCouponName())
                .discountRate(entity.getDiscountRate())
                .usedDate(entity.getUsedDate())
                .userId(entity.getUserId())
                .startedDate(entity.getStartedDate())
                .endedDate(entity.getEndedDate())
                .creationUser(entity.getCreationUser())
                .deletedStatus(entity.getDeletedStatus())
                .build();
    }
}
