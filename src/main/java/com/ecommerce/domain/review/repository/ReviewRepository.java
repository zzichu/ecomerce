package com.ecommerce.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.domain.review.entity.ReviewEntity;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByItemIdAndDeletedStatus(Long itemId, int deletedStatus);
}
