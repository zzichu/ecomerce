package com.ecomerce.domain.purchase.id;

import java.io.Serializable;
import java.util.Objects;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode

public class PurchaseItemId implements Serializable {
    private Long purchaseId;
    private Long itemId;
}
