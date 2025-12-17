package com.ecommerce.domain.purchase.service;

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

import com.ecommerce.domain.item.repository.ItemRepository;
import com.ecommerce.domain.purchase.entity.PurchaseDetailEntity;
import com.ecommerce.domain.purchase.entity.PurchaseItemEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.domain.coupon.repository.CouponRepository;
import com.ecommerce.domain.item.entity.ItemOptionEntity;
import com.ecommerce.domain.item.repository.ItemOptionRepository;
import com.ecommerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecommerce.domain.purchase.dto.PurchaseItemDto;
import com.ecommerce.domain.purchase.enums.DeliveryStatus;
import com.ecommerce.domain.purchase.repository.PurchaseDetailRepository;
import com.ecommerce.domain.purchase.repository.PurchaseItemRepository;

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
    public void purchaseItemTestThreads() throws InterruptedException {
        int threadCount = 12;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            executor.submit(() -> {
                try {
                    ItemRepository itemRepo = mock(ItemRepository.class);
                    PurchaseItemRepository purchaseItemRepo = mock(PurchaseItemRepository.class);
                    PurchaseDetailRepository purchaseDetailRepo = mock(PurchaseDetailRepository.class);
                    ItemOptionRepository itemOptionRepo = mock(ItemOptionRepository.class);

                    PurchaseService threadService = new PurchaseService(
                            itemRepo,
                            purchaseItemRepo,
                            purchaseDetailRepo,
                            itemOptionRepo
                    );

                    AtomicInteger threadStock = new AtomicInteger(9);

                    given(itemOptionRepo.findById(2L)).willAnswer(invocation -> {
                        ItemOptionEntity option = new ItemOptionEntity();
                        option.setOptionId(2L);
                        option.setOptionQuantity(threadStock.get());
                        return Optional.of(option);
                    });

                    given(itemOptionRepo.save(any(ItemOptionEntity.class))).willAnswer(invocation -> {
                        ItemOptionEntity option = invocation.getArgument(0);
                        int currentStock = threadStock.get();
                        if (currentStock <= 0) {
                            throw new IllegalArgumentException("재고가 부족합니다.");
                        }
                        option.setOptionQuantity(threadStock.decrementAndGet());
                        return option;
                    });

                    given(purchaseDetailRepo.save(any(PurchaseDetailEntity.class))).willAnswer(invocation -> {
                        PurchaseDetailEntity saved = invocation.getArgument(0);
                        saved.setPurchaseId(1000L + threadIndex);
                        return saved;
                    });

                    given(purchaseItemRepo.save(any(PurchaseItemEntity.class)))
                            .willAnswer(invocation -> invocation.getArgument(0));

                    PurchaseItemDto purchaseItemDto = new PurchaseItemDto();
                    purchaseItemDto.setOptionId(2L);
                    purchaseItemDto.setPurchaseId(1000L + threadIndex);

                    PurchaseDetailDto purchaseDetailDto = new PurchaseDetailDto();
                    purchaseDetailDto.setQuantity(1);
                    purchaseDetailDto.setUserId(2L);
                    purchaseDetailDto.setPurchaseDate(LocalDateTime.now());
                    purchaseDetailDto.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
                    purchaseDetailDto.setPurchaseId(1000L + threadIndex);

                    boolean success = threadService.purchaseItem(purchaseItemDto, purchaseDetailDto);
                    System.out.println("스레드 " + threadIndex + ": " + success + ", 재고: " + threadStock.get() + "");

                    if (success) {
                        successCount.incrementAndGet();
                    }

                } catch (Exception e) {
                    System.out.println("스레드 " + threadIndex + " 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("구매 성공 수: " + successCount.get());
        assertThat(successCount.get()).isEqualTo(9);
    }

    @Test
    public void purchaseItemTestSequence() {
        AtomicInteger stock = new AtomicInteger(9);

        given(itemOptionRepository.findById(2L)).willAnswer(inv -> {
            ItemOptionEntity option = new ItemOptionEntity();
            option.setOptionId(2L);
            option.setOptionQuantity(stock.get());
            return Optional.of(option);
        });

        given(itemOptionRepository.save(any())).willAnswer(inv -> {
            ItemOptionEntity option = inv.getArgument(0);
            if (stock.get() <= 0) throw new IllegalArgumentException("재고 부족");
            option.setOptionQuantity(stock.decrementAndGet());
            return option;
        });

        given(purchaseDetailRepository.save(any())).willAnswer(inv -> {
            PurchaseDetailEntity saved = inv.getArgument(0);
            saved.setPurchaseId(999L);
            return saved;
        });
        given(purchaseItemRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        int successCount = 0;
        for (int i = 0; i < 12; i++) {
            try {
                PurchaseItemDto dto = new PurchaseItemDto();
                dto.setOptionId(2L);
                dto.setPurchaseId(5L + i);

                PurchaseDetailDto detail = new PurchaseDetailDto();
                detail.setQuantity(1);
                detail.setUserId(2L);
                detail.setPurchaseDate(LocalDateTime.now());
                detail.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
                detail.setPurchaseId(5L + i);

                boolean success = purchaseService.purchaseItem(dto, detail);
                if (success) successCount++;
                System.out.println("순차 " + (i+1) + "번: " + success + " (재고: " + stock.get() + ")");
            } catch (Exception e) {
                System.out.println("순차 " + (i+1) + "번 실패: " + e.getMessage());
            }
        }

        assertThat(successCount).isEqualTo(9);
    }


}
