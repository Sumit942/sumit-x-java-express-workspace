package com.mytextile.inventory.mapper;

import com.mytextile.inventory.dto.InventoryItemDto;
import com.mytextile.inventory.entity.InventoryItem;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InventoryItemMapper {

    public InventoryItemDto toDto(InventoryItem inventoryItem) {
        if (Objects.isNull(inventoryItem)) {
            return null;
        }
        return new InventoryItemDto(
                inventoryItem.getItemId(),
                inventoryItem.getSku(),
                inventoryItem.getName(),
                inventoryItem.getType(),
                inventoryItem.getUnit()
        );
    }

    public InventoryItem toEntity(InventoryItemDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setSku(dto.sku());
        inventoryItem.setName(dto.name());
        inventoryItem.setType(dto.type());
        inventoryItem.setUnit(dto.unit());
        return inventoryItem;
    }
}
