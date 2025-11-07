package com.mytextile.inventory.service;

import com.mytextile.inventory.dto.InventoryItemDto;

import java.util.List;

public interface I_InventoryService {

    InventoryItemDto createItem(InventoryItemDto inventoryItemDto);
    InventoryItemDto getItemById(Long itemId);
    List<InventoryItemDto> getAllItems();
}
