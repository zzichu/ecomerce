package com.ecommerce.domain.item.repository;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.item.entity.ItemOptionEntity;

@Repository
public interface ItemOptionRepository extends JpaRepository<ItemOptionEntity, Long> {
    // 비관적 락 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT io FROM ItemOptionEntity io WHERE io.optionId = :optionId")
    Optional<ItemOptionEntity> findByIdWithPessimisticLock(@Param("optionId") Long optionId);

    // Atomic 재고 감소
    @Modifying
    @Query("UPDATE ItemOptionEntity io SET io.optionQuantity = io.optionQuantity - :purchaseQuantity " +
            "WHERE io.optionId = :optionId AND io.optionQuantity >= :purchaseQuantity")
    int decrementStock(@Param("optionId") Long optionId, @Param("purchaseQuantity") int purchaseQuantity);
}
