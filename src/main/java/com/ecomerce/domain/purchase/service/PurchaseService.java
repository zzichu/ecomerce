package com.ecomerce.domain.purchase.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecomerce.domain.item.repository.ItemOptionRepository;
import com.ecomerce.domain.item.repository.ItemRepository;
import com.ecomerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecomerce.domain.purchase.dto.PurchaseItemDto;
import com.ecomerce.domain.purchase.entity.PurchaseDetailEntity;
import com.ecomerce.domain.purchase.entity.PurchaseItemEntity;
import com.ecomerce.domain.purchase.enums.DeliveryStatus;
import com.ecomerce.domain.purchase.repository.PurchaseDetailRepository;
import com.ecomerce.domain.purchase.repository.PurchaseItemRepository;
import com.ecomerce.domain.review.repository.ReviewRepository;
import com.ecomerce.domain.item.entity.ItemEntity;
import com.ecomerce.domain.item.entity.ItemOptionEntity;

@Service
public class PurchaseService {
	
    private final ItemRepository itemRepository;
    
    private final PurchaseItemRepository purchaseItemRepository;

    private final PurchaseDetailRepository purchaseDetailRepository;

    private final ItemOptionRepository itemOptionRepository;

    public PurchaseService(ItemRepository itemRepository, PurchaseItemRepository purchaseItemRepository, 
                    PurchaseDetailRepository purchaseDetailRepository, ItemOptionRepository itemOptionRepository) {
        this.itemRepository = itemRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseDetailRepository = purchaseDetailRepository;
        this.itemOptionRepository = itemOptionRepository;
    }

    @Transactional
    public boolean purchaseItem(PurchaseItemDto purchaseItemDto, PurchaseDetailDto purchaseDetailDto) {

        ItemOptionEntity option = itemOptionRepository.findById(purchaseItemDto.getOptionId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다."));

        int currentQuantity = option.getOptionQuantity();
        int purchaseQty = purchaseDetailDto.getQuantity();

        if (currentQuantity < purchaseQty) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        option.setOptionQuantity(currentQuantity - purchaseQty);
        itemOptionRepository.save(option);

        PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
            .userId(purchaseDetailDto.getUserId())
            .purchaseDate(purchaseDetailDto.getPurchaseDate())
            .deliveryStatus(purchaseDetailDto.getDeliveryStatus())
            .quantity(purchaseDetailDto.getQuantity())
            .build();

        purchaseDetail = purchaseDetailRepository.save(purchaseDetail);

        PurchaseItemEntity purchaseItem = PurchaseItemEntity.builder()
            .purchaseId(purchaseDetail.getPurchaseId())
            .optionId(purchaseItemDto.getOptionId())
            .build();

        purchaseItemRepository.save(purchaseItem);

        return true;
    }

    @Transactional(readOnly = true)
    public List<PurchaseDetailDto> getAllPurchases() {
        return purchaseDetailRepository.findAll().stream()
            .map(detail -> PurchaseDetailDto.builder()
                .purchaseId(detail.getPurchaseId())
                .userId(detail.getUserId())
                .purchaseDate(detail.getPurchaseDate())
                .deliveryStatus(detail.getDeliveryStatus())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseDetailDto> getPurchaseById(Long purchaseId) {
        return purchaseDetailRepository.findById(purchaseId)
            .map(detail -> PurchaseDetailDto.builder()
                .purchaseId(detail.getPurchaseId())
                .userId(detail.getUserId())
                .purchaseDate(detail.getPurchaseDate())
                .deliveryStatus(detail.getDeliveryStatus())
                .build());
    }

    @Transactional
    public boolean updateDeliveryStatus(Long purchaseId, DeliveryStatus newStatus) {
        PurchaseDetailEntity entity = purchaseDetailRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 내역입니다."));
        entity.changeDeliveryStatus(newStatus);
        purchaseDetailRepository.save(entity);
        return true;
    }

}
