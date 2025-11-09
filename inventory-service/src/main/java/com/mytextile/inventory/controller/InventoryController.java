package com.mytextile.inventory.controller;

import com.mytextile.inventory.dto.*;
import com.mytextile.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // --- Item Catalog Management ---

    @PostMapping("/items")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody CreateItemDto dto) {
        ItemDto newItem = inventoryService.createItem(dto);
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

    @GetMapping("/items/{sku}")
    public ResponseEntity<ItemDto> getItemBySku(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getItemBySku(sku));
    }

    // --- Stock & Ledger Management ---

    @PostMapping("/inward")
    public ResponseEntity<Void> receiveRawMaterial(@Valid @RequestBody InwardRequestDto dto) {
        inventoryService.receiveRawMaterial(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED); // Accepted, processing async
    }

    @PostMapping("/adjust")
    public ResponseEntity<Void> adjustStock(@Valid @RequestBody StockAdjustmentDto dto) {
        inventoryService.adjustStock(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/stock/{sku}")
    public ResponseEntity<StockLevelDto> getStockLevel(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getStockLevel(sku));
    }

    @GetMapping("/history/{sku}")
    public ResponseEntity<List<LedgerEntryDto>> getLedgerHistory(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getLedgerHistory(sku));
    }
}