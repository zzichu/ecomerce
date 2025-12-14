package com.ecomerce.domain.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ecomerce.domain.item.dto.ItemOptionDto;
import com.ecomerce.domain.item.entity.ItemOptionEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecomerce.domain.item.dto.ItemDto;
import com.ecomerce.domain.item.entity.ItemEntity;
import com.ecomerce.domain.item.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @InjectMocks //TODO: InjectMocks와 Mock의 차이
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        // builder 패턴 공통화
    }
    @Test
    @DisplayName("item 생성 테스트")
    void createItem() {
        ItemOptionDto option1 = ItemOptionDto.builder()
                .optionName("M")
                .optionQuantity(10)
                .itemId(2L)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .deletedStatus(false)
                .creationUser("testuser")
                .modificationUser("testuser")
                .build();

        ItemOptionDto option2 = ItemOptionDto.builder()
                .optionName("L")
                .optionQuantity(10)
                .itemId(2L)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .deletedStatus(false)
                .creationUser("testuser")
                .modificationUser("testuser")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .itemId(2L)
                .itemName("티셔츠")
                .itemPrice(10000)
                .description("Good")
                .itemImageUrl("/url")
                .deletedStatus(false)
                .creationUser("testuser")
                .modificationUser("testuser")
                .options(List.of(option1, option2))
                .build();

        given(itemRepository.existsById(2L)).willReturn(false);
        given(itemRepository.save(any(ItemEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        itemService.createItem(itemDto);

        verify(itemRepository, times(1)).existsById(2L);
        verify(itemRepository, times(1)).save(any());
    }
    @Test
    void getAllItems() {
        // given - 옵션 포함된 ItemEntity 2개
        ItemEntity item1 = ItemEntity.builder()
                .itemId(1L)
                .itemName("티셔츠")
                .build();
        ItemOptionEntity option1_1 = ItemOptionEntity.builder()
                .optionId(1L).optionName("M").optionQuantity(10).item(item1).build();
        ItemOptionEntity option1_2 = ItemOptionEntity.builder()
                .optionId(2L).optionName("L").optionQuantity(5).item(item1).build();
        item1.setOptions(Arrays.asList(option1_1, option1_2));

        ItemEntity item2 = ItemEntity.builder()
                .itemId(2L)
                .itemName("바지")
                .build();
        ItemOptionEntity option2_1 = ItemOptionEntity.builder()
                .optionId(3L).optionName("28").optionQuantity(8).item(item2).build();
        item2.setOptions(Arrays.asList(option2_1));

        given(itemRepository.findAllByDeletedStatusFalse())
                .willReturn(Arrays.asList(item1, item2));

        // when
        List<ItemDto> result = itemService.getAllItems();

        // then
        assertThat(result).hasSize(2);

        // item1 검증
        ItemDto itemDto1 = result.get(0);
        assertThat(itemDto1.getItemId()).isEqualTo(1L);
        assertThat(itemDto1.getItemName()).isEqualTo("티셔츠");
        assertThat(itemDto1.getOptions()).hasSize(2);
        assertThat(itemDto1.getOptions().get(0).getOptionName()).isEqualTo("M");
        assertThat(itemDto1.getOptions().get(0).getOptionQuantity()).isEqualTo(10);

        // item2 검증
        ItemDto itemDto2 = result.get(1);
        assertThat(itemDto2.getOptions()).hasSize(1);
        assertThat(itemDto2.getOptions().get(0).getOptionName()).isEqualTo("28");

        verify(itemRepository, times(1)).findAllByDeletedStatusFalse();
    }


    @Test
    void getItemById() {
        // given - 옵션 포함된 단건
        ItemEntity item = ItemEntity.builder()
                .itemId(1L)
                .itemName("청바지")
                .itemPrice(50000)
                .description("좋은 청바지")
                .build();
        ItemOptionEntity option1 = ItemOptionEntity.builder()
                .optionId(1L).optionName("28인치").optionQuantity(3).item(item).build();
        ItemOptionEntity option2 = ItemOptionEntity.builder()
                .optionId(2L).optionName("30인치").optionQuantity(7).item(item).build();
        item.setOptions(Arrays.asList(option1, option2));

        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));

        // when
        ItemDto result = itemService.getItemById(1L);

        // then
        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getItemName()).isEqualTo("청바지");
        assertThat(result.getItemPrice()).isEqualTo(50000L);
        assertThat(result.getOptions()).hasSize(2);
        assertThat(result.getOptions().get(0).getOptionName()).isEqualTo("28인치");
        assertThat(result.getOptions().get(1).getOptionQuantity()).isEqualTo(7);

        verify(itemRepository, times(1)).findById(1L);
    }
    //TODO: N+1 방식 여러개 중에 하나를 택하여 구현하고 쓴 이유 설명
    //동시성을 발생 시켜서 실패하는 테스트 코드, 성공하는 테스트 코드 각각 1개
    //성공한 테스트 코드를 실제 서비스 코드로 구현
}

