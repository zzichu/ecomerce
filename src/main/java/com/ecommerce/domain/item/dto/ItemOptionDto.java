package com.ecommerce.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemOptionDto {
    
    private Long optionId;
    private String optionName;
    private int optionQuantity;
    private Long itemId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Boolean deletedStatus;
    private String creationUser;
    private String modificationUser;
}
