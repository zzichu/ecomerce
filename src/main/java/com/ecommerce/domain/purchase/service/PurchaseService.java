package com.ecommerce.domain.purchase.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.domain.item.dto.ItemOptionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.domain.item.repository.ItemOptionRepository;
import com.ecommerce.domain.item.repository.ItemRepository;
import com.ecommerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecommerce.domain.purchase.dto.PurchaseItemDto;
import com.ecommerce.domain.purchase.entity.PurchaseDetailEntity;
import com.ecommerce.domain.purchase.entity.PurchaseItemEntity;
import com.ecommerce.domain.purchase.enums.DeliveryStatus;
import com.ecommerce.domain.purchase.repository.PurchaseDetailRepository;
import com.ecommerce.domain.purchase.repository.PurchaseItemRepository;
import com.ecommerce.domain.item.entity.ItemOptionEntity;

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
        Long optionId = purchaseItemDto.getOptionId();
        int purchaseQuantity = purchaseDetailDto.getQuantity();

        // TODO: 분산락에 대해서도 공부하고 적용(나중에)
        ItemOptionEntity option = itemOptionRepository.findByIdWithPessimisticLock(optionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다."));

        if (option.getOptionQuantity() < purchaseQuantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        int updatedRows = itemOptionRepository.decrementStock(optionId, purchaseQuantity);

        if (updatedRows == 0) {
            throw new IllegalArgumentException("재고가 부족합니다. (동시 구매 충돌)");
        }

        PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
                .userId(purchaseDetailDto.getUserId())
                .purchaseDate(purchaseDetailDto.getPurchaseDate())
                .deliveryStatus(purchaseDetailDto.getDeliveryStatus())
                .quantity(purchaseQuantity)
                .build();

        purchaseDetail = purchaseDetailRepository.save(purchaseDetail);

        PurchaseItemEntity purchaseItem = PurchaseItemEntity.builder()
                .purchaseId(purchaseDetail.getPurchaseId())
                .optionId(optionId)
                .build();

        purchaseItemRepository.save(purchaseItem);

        return true;
    }

    //TODO: N+1 문제 한번 더 찾아보기


    @Transactional(readOnly = true)
    public List<PurchaseDetailDto> getAllPurchases() {
        return purchaseDetailRepository.findAll().stream()
                .map(detail -> {
                    List<PurchaseItemEntity> purchaseItems = purchaseItemRepository.findByPurchaseId(detail.getPurchaseId());

                    List<ItemOptionDto> optionDtos = purchaseItems.stream()
                            .map(purchaseItem -> {
                                ItemOptionEntity option = itemOptionRepository.findById(purchaseItem.getOptionId())
                                        .orElseThrow(() -> new IllegalArgumentException("옵션 없음"));
                                return ItemOptionDto.builder()
                                        .optionId(option.getOptionId())
                                        .optionName(option.getOptionName())
                                        .optionQuantity(option.getOptionQuantity())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return PurchaseDetailDto.builder()
                            .purchaseId(detail.getPurchaseId())
                            .userId(detail.getUserId())
                            .purchaseDate(detail.getPurchaseDate())
                            .deliveryStatus(detail.getDeliveryStatus())
                            .options(optionDtos)
                            .build();
                })
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
