package com.ecommerce.domain.purchase.id;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode

public class PurchaseItemId implements Serializable {
    private Long purchaseId;
    private Long itemId;
}
