package com.ecomerce.domain.purchase.entity;

import com.ecomerce.domain.item.dto.ItemOptionDto;
import com.ecomerce.domain.item.entity.ItemOptionEntity;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "purchase_item")
@IdClass(PurchaseCompositeId.class)
public class PurchaseItemEntity {
    @Id
    private Long purchaseId;
    @Id private Long optionId;
}
