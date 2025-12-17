package com.ecommerce.domain.purchase.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.domain.item.dto.ItemOptionDto;
import com.ecommerce.domain.purchase.enums.DeliveryStatus;
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
public class PurchaseDetailDto {
    private Long purchaseId;
    private Long userId;
    private LocalDateTime purchaseDate;
    private DeliveryStatus deliveryStatus;
    private int quantity;
    private List<ItemOptionDto> options;

}
