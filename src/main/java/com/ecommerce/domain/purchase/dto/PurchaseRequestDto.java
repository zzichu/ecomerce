package com.ecommerce.domain.purchase.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PurchaseRequestDto {
    private PurchaseItemDto purchaseItemDto;
    private PurchaseDetailDto purchaseDetailDto;
}