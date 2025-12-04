package com.ecomerce.domain.purchase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.ecomerce.domain.purchase.entity.PurchaseDetailEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecomerce.domain.coupon.repository.CouponRepository;
import com.ecomerce.domain.item.entity.ItemOptionEntity;
import com.ecomerce.domain.item.repository.ItemOptionRepository;
import com.ecomerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecomerce.domain.purchase.dto.PurchaseItemDto;
import com.ecomerce.domain.purchase.enums.DeliveryStatus;
import com.ecomerce.domain.purchase.repository.PurchaseDetailRepository;
import com.ecomerce.domain.purchase.repository.PurchaseItemRepository;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private ItemOptionRepository itemOptionRepository;
    @Mock
    private PurchaseItemRepository purchaseItemRepository;
    @Mock
    private PurchaseDetailRepository purchaseDetailRepository;
    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    public void purchaseItemTest() {
        ItemOptionEntity itemOption = new ItemOptionEntity();
        itemOption.setOptionId(2L);
        itemOption.setOptionQuantity(9);

        given(itemOptionRepository.findById(2L))
                .willReturn(Optional.of(itemOption));

        given(itemOptionRepository.save(any(ItemOptionEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(purchaseDetailRepository.save(any()))
                .willAnswer(invocation -> {
                    PurchaseDetailEntity saved = invocation.getArgument(0);
                    saved.setPurchaseId(999L);
                    return saved;
                });
        given(purchaseItemRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        int threadCount = 12;
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            long purchaseId = 5 + i;

            PurchaseItemDto purchaseItemDto = new PurchaseItemDto();
            purchaseItemDto.setPurchaseId(purchaseId);
            purchaseItemDto.setOptionId(2L);

            PurchaseDetailDto purchaseDetailDto = new PurchaseDetailDto();
            purchaseDetailDto.setPurchaseId(purchaseId);
            purchaseDetailDto.setUserId(2L);
            purchaseDetailDto.setPurchaseDate(LocalDateTime.now());
            purchaseDetailDto.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
            purchaseDetailDto.setQuantity(1);

            try {
                boolean success = purchaseService.purchaseItem(purchaseItemDto, purchaseDetailDto);
                System.out.println("시도 " + (i + 1) + "번 - 구매 성공: " + success);

                if (success) {
                    successCount.incrementAndGet();
                }
            } catch (Exception e) {
                System.out.println("시도 " + (i + 1) + "번 - 실패: " + e.getMessage());
            }
        }

        System.out.println("구매 성공 수: " + successCount.get());
    }

}
