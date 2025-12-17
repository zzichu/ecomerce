package com.ecommerce;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import com.ecommerce.domain.coupon.repository.CouponRepository;
import com.ecommerce.domain.item.repository.ItemOptionRepository;
import com.ecommerce.domain.item.repository.ItemRepository;
import com.ecommerce.domain.purchase.dto.PurchaseDetailDto;
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

//    private void createTestData() {
//        // PurchaseDetail 3개
//        PurchaseDetailEntity detail1 = PurchaseDetailEntity.builder()
//                .purchaseId(3L).userId(2L).quantity(1).build();
//        PurchaseDetailEntity detail2 = PurchaseDetailEntity.builder()
//                .purchaseId(4L).userId(2L).quantity(2).build();
//        PurchaseDetailEntity detail3 = PurchaseDetailEntity.builder()
//                .purchaseId(5L).userId(2L).quantity(1).build();
//
//        // ItemOption 3개
//        ItemOptionEntity option1 = ItemOptionEntity.builder()
//                .optionId(1L).optionName("item4-option1").optionQuantity(5).build();
//        ItemOptionEntity option2 = ItemOptionEntity.builder()
//                .optionId(1L).optionName("item4-option1").optionQuantity(3).build();
//        ItemOptionEntity option3 = ItemOptionEntity.builder()
//                .optionId(1L).optionName("item4-option1").optionQuantity(8).build();
//
//        // PurchaseItem 3개 (각각 다른 옵션)
//        PurchaseItemEntity item1 = PurchaseItemEntity.builder()
//                .purchaseId(3L).optionId(1L).build();
//        PurchaseItemEntity item2 = PurchaseItemEntity.builder()
//                .purchaseId(4L).optionId(1L).build();
//        PurchaseItemEntity item3 = PurchaseItemEntity.builder()
//                .purchaseId(5L).optionId(1L).build();
//
//        // 저장
//        entityManager.persistAndFlush(option1);
//        entityManager.persistAndFlush(option2);
//        entityManager.persistAndFlush(option3);
//        entityManager.persistAndFlush(detail1);
//        entityManager.persistAndFlush(detail2);
//        entityManager.persistAndFlush(detail3);
//        entityManager.persistAndFlush(item1);
//        entityManager.persistAndFlush(item2);
//        entityManager.persistAndFlush(item3);
//    }
//    //TODO: API요청시 N+1 발생하는 지, 어디서 발생하는 지 찾아볼 것
//	@Test
//	public void testNPlusOneOnItemOptions() {
//		List<ItemEntity> items = itemRepository.findAll();
//
//		for (ItemEntity item : items) {
//			int optionCount = item.getOptions().size();
//			System.out.println("Item 옵션 count: " + optionCount);
//		}
//	}
//	@Test
//	public void testNPlusOneOnPurchaseDetails() {
//		List<PurchaseItemEntity> purchaseItems = purchaseItemRepository.findAll();
//
//		for (PurchaseItemEntity purchaseItem : purchaseItems) {
//			Long detailsCount = purchaseItem.getPurchaseId();
//			System.out.println("Purchase detail count: " + detailsCount);
//		}
//	}
//
//	@Test
//	public void testConcurrentPurchase() throws InterruptedException {
//		int threadCount = 9;
//		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//		AtomicInteger successCount = new AtomicInteger(0);
//		AtomicLong purchaseIdGenerator = new AtomicLong(5);
//		CountDownLatch startLatch = new CountDownLatch(1);
//		CountDownLatch doneLatch = new CountDownLatch(threadCount);
//        //TODO: latch 쓰는 이유
//		PurchaseItemDto purchaseItemDto = new PurchaseItemDto();
//		purchaseItemDto.setOptionId((long) 2);
//
//		PurchaseDetailDto purchaseDetailDto = new PurchaseDetailDto();
//		purchaseDetailDto.setUserId((long) 2);
//		purchaseDetailDto.setPurchaseDate(LocalDateTime.now());
//		purchaseDetailDto.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
//		purchaseDetailDto.setQuantity(1);
//
//		for (int i = 0; i < threadCount; i++) {
//			executor.submit(() -> {
//				try {
//					startLatch.await();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				long purchaseId = purchaseIdGenerator.getAndIncrement();
//
//				PurchaseItemDto threadPurchaseItemDto = new PurchaseItemDto();
//				threadPurchaseItemDto.setPurchaseId(purchaseId);
//				threadPurchaseItemDto.setOptionId((long) 2);
//
//				PurchaseDetailDto threadPurchaseDetailDto = new PurchaseDetailDto();
//				threadPurchaseDetailDto.setPurchaseId(purchaseId);
//				threadPurchaseDetailDto.setUserId((long) 2);
//				threadPurchaseDetailDto.setPurchaseDate(LocalDateTime.now());
//				threadPurchaseDetailDto.setDeliveryStatus(DeliveryStatus.BEFORE_DELIVERY);
//				threadPurchaseDetailDto.setQuantity(1);
//
//				try {
//					boolean success = purchaseService.purchaseItem(threadPurchaseItemDto, threadPurchaseDetailDto);
//					System.out.println("구매 성공 여부: " + success);
//
//					if (success) {
//						successCount.incrementAndGet();
//					}
//    			} catch (Exception e) {
//       			 e.printStackTrace();
//				} finally {
//					doneLatch.countDown();
//				}
//			});
//		}
//		startLatch.countDown();
//		doneLatch.await();
//		executor.shutdown();
//		System.out.println("구매 성공 수: " + successCount.get());
//	}

}
