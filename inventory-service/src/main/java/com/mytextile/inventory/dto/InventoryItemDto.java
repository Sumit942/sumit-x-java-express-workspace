package com.mytextile.inventory.dto;

import com.mytextile.inventory.entity.ItemType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InventoryItemDto(
        Long itemId,

        @NotEmpty(message = "Stock keeping unit name cannot be empty")
        String sku,

        @NotEmpty(message = "Item name cannot be empty")
        String name,

        @NotNull(message = "Please select Item type")
        ItemType type,

        @NotEmpty(message = "Unit cannot be empty")
        String unit
) {
}
