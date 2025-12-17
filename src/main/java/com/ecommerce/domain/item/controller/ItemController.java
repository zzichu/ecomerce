package com.ecommerce.domain.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.domain.item.dto.ItemDto;
import com.ecommerce.domain.item.service.ItemService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ItemDto>> create(@RequestBody ItemDto itemDto) {
        itemService.createItem(itemDto);
        ApiResponse<ItemDto> response = new ApiResponse<>(true, "상품 등록 성공", itemDto);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse<ItemDto>> update(@RequestBody ItemDto itemDto) {
        ItemDto updatedItem = itemService.updateItem(itemDto);
        ApiResponse<ItemDto> response = new ApiResponse<>(true, "상품 수정 성공", updatedItem);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemDto>> delete(@PathVariable("itemId") Long itemId) {
        ItemDto deletedItem = itemService.deleteItem(itemId);
        ApiResponse<ItemDto> response = new ApiResponse<>(true, "상품 삭제 성공", deletedItem);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ItemDto>>> getAll() {
        List<ItemDto> itemList = itemService.getAllItems();
        ApiResponse<List<ItemDto>> response = new ApiResponse<>(true, "상품 전체 조회 성공", itemList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemDto>> getOne(@PathVariable("itemId") Long itemId) {
        ItemDto item = itemService.getItemById(itemId);
        ApiResponse<ItemDto> response = new ApiResponse<>(true, "상품 조회 성공", item);
        return ResponseEntity.ok(response);
    }


}

