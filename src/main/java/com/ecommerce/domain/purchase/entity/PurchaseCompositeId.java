package com.ecommerce.domain.purchase.entity;

import java.io.Serializable;

import lombok.Data;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class PurchaseCompositeId implements Serializable{

    private Long purchaseId;

    private Long optionId;

}
