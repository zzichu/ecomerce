package com.ecomerce.domain.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecomerce.domain.purchase.entity.PurchaseDetailEntity;

import java.util.List;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetailEntity, Long> {
    @Query("SELECT DISTINCT pd FROM PurchaseDetailEntity pd " +
            "LEFT JOIN FETCH PurchaseItemEntity pi ON pd.purchaseId = pi.purchaseId " +
            "LEFT JOIN FETCH ItemOptionEntity io ON pi.optionId = io.optionId")
    List<PurchaseDetailEntity> findAllWithFetchJoin();
}
