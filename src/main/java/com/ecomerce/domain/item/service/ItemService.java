package com.ecomerce.domain.item.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecomerce.domain.item.repository.ItemRepository;
import com.ecomerce.domain.item.dto.ItemDto;
import com.ecomerce.domain.item.entity.ItemEntity;
import com.ecomerce.domain.item.entity.ItemOptionEntity;

@Service
public class ItemService {
    
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    // 상품 등록
    @Transactional
    public void createItem(ItemDto itemDto) {
        if (itemDto.getItemId() != null && itemRepository.existsById(itemDto.getItemId())) {
            throw new IllegalArgumentException("이미 존재하는 아이템입니다.");
        }

        ItemEntity item = ItemEntity.builder()
	                .itemName(itemDto.getItemName())
	                .itemPrice(itemDto.getItemPrice())
	                .description(itemDto.getDescription())
	                .itemImageUrl(itemDto.getItemImageUrl())
	                .creationUser(itemDto.getCreationUser())
	                .modificationUser(itemDto.getModificationUser())
	                .deletedStatus(itemDto.getDeletedStatus() != null ? itemDto.getDeletedStatus() : false)
	                .createdDate(itemDto.getCreatedDate())
	                .modifiedDate(itemDto.getModifiedDate())
	                .build(); //불변성이 있어서, 가독성 좋음

        itemRepository.save(item);

        List<ItemOptionEntity> optionEntities = itemDto.getOptions().stream()
            .map(optionDto -> {
                ItemOptionEntity optionEntity = ItemOptionEntity.builder()
                    .optionName(optionDto.getOptionName())
                    .optionQuantity(optionDto.getOptionQuantity())
                    .item(item)   // 연관관계 설정
                    .createdDate(optionDto.getCreatedDate())
                    .modifiedDate(optionDto.getModifiedDate())
                    .deletedStatus(optionDto.getDeletedStatus() != null ? optionDto.getDeletedStatus() : false)
                    .creationUser(optionDto.getCreationUser())
                    .modificationUser(optionDto.getModificationUser())
                    .build();
                return optionEntity;
            }).collect(Collectors.toList());

        item.setOptions(optionEntities); //TODO: 더티체킹 변경감지 addoption으로 바꾸기

    }
    
    // 상품 수정
    @Transactional
    public ItemDto updateItem(ItemDto itemDto) {
        Long itemId = itemDto.getItemId();
        if (itemId == null) {
            throw new IllegalArgumentException("아이템 ID가 없습니다.");
        }

        ItemEntity item = itemRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));
        // TODO: setter대신 정적팩토리 메소드 쓰는 이유 (수정 완료)
        item.update(itemDto);
        itemRepository.save(item);

        return ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .description(item.getDescription())
                .itemImageUrl(item.getItemImageUrl())
                .creationUser(item.getCreationUser())
                .modificationUser(item.getModificationUser())
                .createdDate(item.getCreatedDate())
                .modifiedDate(item.getModifiedDate())
                .deletedStatus(item.getDeletedStatus())
                .build();
    }
    
    // 상품 삭제
    // TODO : Transactional 차이점 공부!
    @Transactional
    public ItemDto deleteItem(Long itemId) {
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

        itemEntity.setDeletedStatus(true); //TODO: setter 수정
        itemRepository.save(itemEntity);

        return ItemDto.builder()
                .itemId(itemEntity.getItemId())
                .itemName(itemEntity.getItemName())
                .itemPrice(itemEntity.getItemPrice())
                .description(itemEntity.getDescription())
                .itemImageUrl(itemEntity.getItemImageUrl())
                .creationUser(itemEntity.getCreationUser())
                .modificationUser(itemEntity.getModificationUser())
                .createdDate(itemEntity.getCreatedDate())
                .modifiedDate(itemEntity.getModifiedDate())
                .deletedStatus(itemEntity.getDeletedStatus())
                .build();
    }

    // 상품 전체 조회
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems() {
    	// TODO: java map 병렬처리
    	// TODO : for문과 stream의 차이 (조금 더 깊게)
        return itemRepository.findAllByDeletedStatusFalse()
                .stream()
                .map(itemEntity -> ItemDto.builder()
                        .itemId(itemEntity.getItemId())
                        .itemName(itemEntity.getItemName())
                        .itemPrice(itemEntity.getItemPrice())
                        .description(itemEntity.getDescription())
                        .itemImageUrl(itemEntity.getItemImageUrl())
                        .creationUser(itemEntity.getCreationUser())
                        .modificationUser(itemEntity.getModificationUser())
                        .createdDate(itemEntity.getCreatedDate())
                        .modifiedDate(itemEntity.getModifiedDate())
                        .deletedStatus(itemEntity.getDeletedStatus())
                        .options(itemEntity.toOptionDtos())
                        .build())
                .collect(Collectors.toList());
    }

    // 상품 단건 조회
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long itemId) {
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

        return ItemDto.builder()
                .itemId(itemEntity.getItemId())
                .itemName(itemEntity.getItemName())
                .itemPrice(itemEntity.getItemPrice())
                .description(itemEntity.getDescription())
                .itemImageUrl(itemEntity.getItemImageUrl())
                .creationUser(itemEntity.getCreationUser())
                .modificationUser(itemEntity.getModificationUser())
                .createdDate(itemEntity.getCreatedDate())
                .modifiedDate(itemEntity.getModifiedDate())
                .deletedStatus(itemEntity.getDeletedStatus())
                .options(itemEntity.toOptionDtos())
                .build();
    }

}
