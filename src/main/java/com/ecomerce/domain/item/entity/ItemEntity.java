package com.ecomerce.domain.item.entity;

import com.ecomerce.domain.item.dto.ItemOptionDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.ecomerce.domain.item.dto.ItemDto;

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
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name", nullable = false, length = 255)
    private String itemName;

    @Column(name="item_price", nullable = false)
    private int itemPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "item_image_url", length = 500)
    private String itemImageUrl;

    @Column(name = "creation_user", length = 100)
    private String creationUser;

    @Column(name = "created_date")
    @CreatedBy
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modification_user", length = 100)
    private String modificationUser;
   
    //TODO: created date, modified date common으로 빼기
    @Column(name = "modified_date")
    @LastModifiedBy
    @LastModifiedDate
    private LocalDateTime modifiedDate;
   
    @Column(name = "deleted_status")
    private Boolean deletedStatus = false;

    public void update(ItemDto itemDto) {
        this.itemName = itemDto.getItemName();
        this.itemPrice = itemDto.getItemPrice();
        this.description = itemDto.getDescription();
        this.itemImageUrl = itemDto.getItemImageUrl();
        this.modificationUser = itemDto.getModificationUser();
        this.modifiedDate = itemDto.getModifiedDate() != null ? itemDto.getModifiedDate() : LocalDateTime.now();
        this.deletedStatus = itemDto.getDeletedStatus() != null ? itemDto.getDeletedStatus() : this.deletedStatus;
    }

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOptionEntity> options = new ArrayList<>();

    //TODO: itemEntity ->  itemDto 변환 (옵션)
    //팩토리 메소드 패턴 : 정적메소드를 재사용
    public List<ItemOptionDto> toOptionDtos() {
        return this.options.stream()
                .map(option -> ItemOptionDto.builder()
                        .optionId(option.getOptionId())
                        .optionName(option.getOptionName())
                        .optionQuantity(option.getOptionQuantity())
                        .itemId(this.itemId)  // 또는 option.getItem().getItemId()
                        .createdDate(option.getCreatedDate())
                        .modifiedDate(option.getModifiedDate())
                        .deletedStatus(option.getDeletedStatus())
                        .creationUser(option.getCreationUser())
                        .modificationUser(option.getModificationUser())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
