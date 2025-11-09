package com.mytextile.inventory.dto;

import com.mytextile.inventory.model.ItemType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateItemDto(
        @NotEmpty(message = "SKU cannot be empty")
        String sku,
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotNull(message = "Item type cannot be null")
        ItemType type,
        @NotEmpty(message = "Unit cannot be empty")
        String unit
) {
}