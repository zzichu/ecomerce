package com.ecomerce.domain.purchase.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ecomerce.domain.purchase.enums.DeliveryStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class PurchaseCompositeId implements Serializable{

    private Long purchaseId;

    private Long optionId;

}
