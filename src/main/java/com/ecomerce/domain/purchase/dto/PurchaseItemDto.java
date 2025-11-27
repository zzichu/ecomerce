package com.ecomerce.domain.purchase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PurchaseItemDto {
    private Long purchaseId;
    private Long optionId;
}
