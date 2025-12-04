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
    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem() {
        ItemOptionDto option1 = ItemOptionDto.builder()
                .optionName("M")
                .optionQuantity(10)
                .itemId(2L)  // 등록시 null
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
        ItemEntity item1 = ItemEntity.builder()
                .itemId(1L)
                .itemName("티셔츠")
                .build();
        ItemEntity item2 = ItemEntity.builder()
                .itemId(2L)
                .itemName("상품2")
                .build();

        given(itemRepository.findAllByDeletedStatusFalse())
                .willReturn(Arrays.asList(item1, item2));

        List<ItemDto> result = itemService.getAllItems();

        assertThat(result).hasSize(2);
        verify(itemRepository, times(1)).findAllByDeletedStatusFalse();
    }

    @Test
    void getItemById() {
        // given
        ItemEntity item = ItemEntity.builder()
                .itemId(1L)
                .itemName("item1")
                .itemPrice(10000)
                .description("good")
                .build();

        given(itemRepository.findById(1L))
                .willReturn(Optional.of(item));

        ItemDto result = itemService.getItemById(1L);

        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getItemName()).isEqualTo("item1");
        assertThat(result.getItemPrice()).isEqualTo(10000);

        verify(itemRepository, times(1)).findById(1L);
    }
}

