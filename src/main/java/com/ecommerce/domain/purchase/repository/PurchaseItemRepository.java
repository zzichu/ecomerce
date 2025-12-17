package com.ecommerce.domain.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.purchase.entity.PurchaseCompositeId;
import com.ecommerce.domain.purchase.entity.PurchaseItemEntity;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItemEntity, PurchaseCompositeId> {
    List<PurchaseItemEntity> findByPurchaseId(long purchaseId);
}

