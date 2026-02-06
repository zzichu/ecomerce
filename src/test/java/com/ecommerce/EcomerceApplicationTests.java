package com.ecommerce;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import com.ecommerce.domain.coupon.repository.CouponRepository;
import com.ecommerce.domain.item.repository.ItemOptionRepository;
import com.ecommerce.domain.item.repository.ItemRepository;
import com.ecommerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecommerce.domain.purchase.dto.PurchaseItemDto;
import com.ecommerce.domain.purchase.enums.DeliveryStatus;
import com.ecommerce.domain.purchase.service.PurchaseService;
import com.ecommerce.domain.purchase.repository.PurchaseDetailRepository;
import com.ecommerce.domain.purchase.repository.PurchaseItemRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EcomerceApplication.class)
@Transactional  // 🔥 데이터 롤백 보장!
class EcomerceApplicationTests {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;
    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;
    @Autowired
    private ItemOptionRepository itemOptionRepository;

    //TODO: 스프링부트 테스트에서 MockBean이 왜 안되는지 찾아볼것
    //통합 테스트니까 Autowired로 하는게 나음
    //단위테스트로 서비스별로 테스트 코드 작성
    //단위테스트로 만들고 서비스 코드로 만들기
    //TODO: 통합 테스트와 단위 테스트 차이점 찾아볼것
    //TODO: N+1문제 확인해오기

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
	private CouponRepository couponRepository;
    private TestEntityManager entityManager;

    @Test
	void contextLoads() {
	}
    @Test
    void getAllPurchases_NPlus1() {
        // when - N+1 발생!
        List<PurchaseDetailDto> result = purchaseService.getAllPurchases();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getOptions()).hasSize(1);

        // ✅ Hibernate 로그에서 확인:
        // 1. SELECT purchase_detail (1)
        // 2. SELECT purchase_item WHERE purchase_id=? (3번 = N)
        // 3. SELECT item_option WHERE id=? (3번 = N)
        // 총 1 + 3 + 3 = 7쿼리!
    }

	@Test
	public void testConcurrentPurchase() throws InterruptedException {
		int threadCount = 9;
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicLong purchaseIdGenerator = new AtomicLong(5);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);
       //TODO: latch 쓰는 이유 
		PurchaseItemDto purchaseItemDto = new PurchaseItemDto();
		purchaseItemDto.setOptionId((long) 2);

		PurchaseDetailDto purchaseDetailDto = new PurchaseDetailDto();
		purchaseDetailDto.setUserId((long) 2);
		purchaseDetailDto.setPurchaseDate(LocalDateTime.now());
		purchaseDetailDto.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
		purchaseDetailDto.setQuantity(1);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					startLatch.await();
				} catch (InterruptedException e) {
					// TODO : Auto-generated catch block
					e.printStackTrace();
				}
				long purchaseId = purchaseIdGenerator.getAndIncrement();

				PurchaseItemDto threadPurchaseItemDto = new PurchaseItemDto();
				threadPurchaseItemDto.setPurchaseId(purchaseId);
				threadPurchaseItemDto.setOptionId((long) 2);

				PurchaseDetailDto threadPurchaseDetailDto = new PurchaseDetailDto();
				threadPurchaseDetailDto.setPurchaseId(purchaseId);
				threadPurchaseDetailDto.setUserId((long) 2);
				threadPurchaseDetailDto.setPurchaseDate(LocalDateTime.now());
				threadPurchaseDetailDto.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
				threadPurchaseDetailDto.setQuantity(1);

				try {
					boolean success = purchaseService.purchaseItem(threadPurchaseItemDto, threadPurchaseDetailDto);
					System.out.println("구매 성공 여부: " + success);

					if (success) {
						successCount.incrementAndGet();
					}
   			} catch (Exception e) {
      			 e.printStackTrace();
				} finally {
					doneLatch.countDown();
				}
			});
		}
		startLatch.countDown();
		doneLatch.await();
		executor.shutdown();
		System.out.println("구매 성공 수: " + successCount.get());
	}

}
