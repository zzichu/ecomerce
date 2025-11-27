package com.ecomerce.domain.purchase.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.ecomerce.domain.purchase.enums.DeliveryStatus;

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
@Table(name = "purchase_detail")
public class PurchaseDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long purchaseId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private LocalDateTime purchaseDate;

	@Column(name = "delivery_status")
	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

    private int quantity;

	public void changeDeliveryStatus(DeliveryStatus newStatus) {
	    this.deliveryStatus = newStatus;
	}
}
