package com.ecommerce.domain.purchase.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecommerce.domain.purchase.dto.PurchaseRequestDto;
import com.ecommerce.domain.purchase.enums.DeliveryStatus;
import com.ecommerce.domain.purchase.service.PurchaseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    // 상품 구매
    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseRequestDto>> purchaseItem(@RequestBody PurchaseRequestDto purchaseRequestDto) {
        purchaseService.purchaseItem(purchaseRequestDto.getPurchaseItemDto(), purchaseRequestDto.getPurchaseDetailDto());
        ApiResponse<PurchaseRequestDto> response = new ApiResponse<>(true, "구매 성공", purchaseRequestDto);
        return ResponseEntity.ok(response);
    }
    
    // 상품 구매 내역 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<PurchaseDetailDto>>> getAllPurchases() {
        List<PurchaseDetailDto> items = purchaseService.getAllPurchases();
        ApiResponse<List<PurchaseDetailDto>> response = new ApiResponse<>(true, "상품 구매 내역 전체 조회 성공", items);
        return ResponseEntity.ok(response);
    
    }

    // 상품 구매 내역 단건 조회
    @GetMapping("/{purchaseId}")
    public ResponseEntity<ApiResponse<PurchaseDetailDto>> getPurchaseById(@PathVariable("purchaseId") Long purchaseId) {
        Optional<PurchaseDetailDto> purchaseDetailOpt = purchaseService.getPurchaseById(purchaseId);
        if (purchaseDetailOpt.isPresent()) {
            ApiResponse<PurchaseDetailDto> response = new ApiResponse<>(true, "구매 내역 조회 성공", purchaseDetailOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<PurchaseDetailDto> response = new ApiResponse<>(false, "구매 내역을 찾을 수 없습니다", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 구매 내역 배송 상태 수정
    @PatchMapping("/{purchaseId}/status")
    public ResponseEntity<ApiResponse<Void>> updateDeliveryStatus(
            @PathVariable("purchaseId") Long purchaseId, @RequestBody DeliveryStatus newStatus) {
        boolean updated = purchaseService.updateDeliveryStatus(purchaseId, newStatus);
        if (!updated) {
            ApiResponse<Void> response = new ApiResponse<>(false, "구매 내역을 찾을 수 없습니다", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Void> response = new ApiResponse<>(true, "배송 상태 수정 성공", null);
        return ResponseEntity.ok(response);
    }


}

