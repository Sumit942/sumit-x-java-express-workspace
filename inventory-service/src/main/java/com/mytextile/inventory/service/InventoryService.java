package com.mytextile.inventory.service;

import com.mytextile.inventory.dto.*;

import java.util.List;

public interface InventoryService {

    // --- Item (Catalog) Methods ---
    ItemDto createItem(CreateItemDto dto);
    ItemDto getItemBySku(String sku);

    // --- Ledger & Stock Methods ---
    StockLevelDto getStockLevel(String sku);
    List<LedgerEntryDto> getLedgerHistory(String sku);
    void receiveRawMaterial(InwardRequestDto dto);
    void adjustStock(StockAdjustmentDto dto);
}