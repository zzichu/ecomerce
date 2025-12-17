package com.ecommerce.domain.item.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ItemDto {
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private String description;   
    private String itemImageUrl;
    private String creationUser;
    private LocalDateTime createdDate;    
    private String modificationUser;
    private LocalDateTime modifiedDate;    
    private Boolean deletedStatus;
	private List<ItemOptionDto> options;

	// public Long getItemId() {
	// 	return itemId;
	// }
	// public void setItemId(Long itemId) {
	// 	this.itemId = itemId;
	// }
	// public String getItemName() {
	// 	return itemName;
	// }
	// public void setItemName(String itemName) {
	// 	this.itemName = itemName;
	// }
	// public int getItemPrice() {
	// 	return itemPrice;
	// }
	// public void setItemPrice(int itemPrice) {
	// 	this.itemPrice = itemPrice;
	// }
	// public String getDescription() {
	// 	return description;
	// }
	// public void setDescription(String description) {
	// 	this.description = description;
	// }
	// public String getItemImageUrl() {
	// 	return itemImageUrl;
	// }
	// public void setItemImageUrl(String itemImageUrl) {
	// 	this.itemImageUrl = itemImageUrl;
	// }
	// public String getCreationUser() {
	// 	return creationUser;
	// }
	// public void setCreationUser(String creationUser) {
	// 	this.creationUser = creationUser;
	// }
	// public LocalDateTime getCreatedDate() {
	// 	return createdDate;
	// }
	// public void setCreatedDate(LocalDateTime createdDate) {
	// 	this.createdDate = createdDate;
	// }
	// public String getModificationUser() {
	// 	return modificationUser;
	// }
	// public void setModificationUser(String modificationUser) {
	// 	this.modificationUser = modificationUser;
	// }
	// public LocalDateTime getModifiedDate() {
	// 	return modifiedDate;
	// }
	// public void setModifiedDate(LocalDateTime modifiedDate) {
	// 	this.modifiedDate = modifiedDate;
	// }
	// public Boolean getDeletedStatus() {
	// 	return deletedStatus;
	// }
	// public void setDeletedStatus(Boolean deletedStatus) {
	// 	this.deletedStatus = deletedStatus;
	// }         
}
