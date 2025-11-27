package com.ecomerce.domain.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomerce.domain.purchase.entity.PurchaseCompositeId;
import com.ecomerce.domain.purchase.entity.PurchaseItemEntity;
import com.ecomerce.domain.purchase.id.PurchaseItemId;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItemEntity, PurchaseCompositeId> {
	
}

