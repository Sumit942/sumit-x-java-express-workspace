package com.mytextile.inventory.dto;

import com.mytextile.inventory.model.ItemType;

public record ItemDto(
    Long itemId,
    String sku,
    String name,
    ItemType type,
    String unit
) {}